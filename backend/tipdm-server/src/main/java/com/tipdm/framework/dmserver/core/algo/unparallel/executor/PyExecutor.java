package com.tipdm.framework.dmserver.core.algo.unparallel.executor;

import com.tipdm.framework.common.utils.RedisUtils;
import com.tipdm.framework.common.utils.SpringUtils;
import com.tipdm.framework.dmserver.core.algo.unparallel.listener.PyAlgorithmListener;
import com.tipdm.framework.dmserver.core.algo.unparallel.interpreter.PythonInterpreter;
import com.tipdm.framework.dmserver.exception.AlgorithmException;
import com.tipdm.framework.dmserver.pyserve.PySession;
import com.tipdm.framework.dmserver.utils.Constants;
import com.tipdm.framework.model.dmserver.Component;
import com.tipdm.framework.model.dmserver.DataSchema;
import com.tipdm.framework.model.dmserver.Model;
import com.tipdm.framework.model.dmserver.Step;
import com.tipdm.framework.service.dmserver.DataSchemaService;
import com.tipdm.framework.service.dmserver.ModelService;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;

/**
 * Created by TipDM on 2017/6/21.
 * E-mail:devp@tipdm.com
 */
public class PyExecutor implements AlgorithmExecutor{

    private final Logger logger = LoggerFactory.getLogger(PyExecutor.class);

    private static DataSchemaService dataSchemaService = SpringUtils.getBean("dataSchemaService", DataSchemaService.class);
    private static ModelService modelService = SpringUtils.getBean("modelService", ModelService.class);

    private PyAlgorithmListener listener;

    private PySession pySession;

    private Step step = Step.MAIN;

    private transient Component component;

    public PyExecutor(PySession session, Component component){
        this.pySession = session;
        this.component = component;
        this.listener = new PyAlgorithmListener();
    }

    public PyExecutor(PySession session, Component component, Step step){
        this.pySession = session;
        this.component = component;
        this.step = step;
        this.listener = new PyAlgorithmListener();
    }

    @Override
    public void run() throws AlgorithmException{
        ExecutorContext context = new ExecutorContext(this.component, this.pySession);
        AlgorithmException ae = null;
        //
        MDC.put("component", this.component.getClientId().toString());
        listener.init(context);
        try{
            logger.info("载入算法脚本...");
            //执行算法
            if(component.getScript().size() == 0 || !component.getScript().containsKey(step)){
                throw new IllegalArgumentException("算法载入出错，无法读取脚本");
            }
            DataSchema dataSchema = dataSchemaService.findByName(component.getCreatorName());
            String script = component.getScript().get(step);
            String reportFile = component.getClientId() + ".html";
            if(step == Step.MAIN) {
                String model = context.getOutputs().get("model");
                File pyFile = PythonInterpreter.makeTrainFile(dataSchema.getName(), dataSchema.getPassword(), script, context.getInputs(), context.getOutputs(), context.getParams(), reportFile, model);
                pySession.train(pyFile, reportFile, model);
            } else {
                String model = context.getInputs().get("model");
                File file = null;
                if(NumberUtils.isDigits(model)) {
                    Model modelObject = modelService.findOne(Long.valueOf(model));
                    file = new File(RedisUtils.get(Constants.MODEL_DIR, String.class), modelObject.getModelPath());
                } else {
                    file = new File(RedisUtils.get(Constants.MODEL_DIR, String.class), model);
                }
                if (step == Step.PREDICT) {
                    //预测
                    File pyFile = PythonInterpreter.makePredictFile(dataSchema.getName(), dataSchema.getPassword(), script, context.getInputs(), context.getOutputs(), context.getParams(), reportFile, file);
                    pySession.predict(pyFile, reportFile, file);
                } else {
                    File pyFile = PythonInterpreter.makeEvalFile(dataSchema.getName(), dataSchema.getPassword(), script, context.getInputs(), context.getOutputs(), context.getParams(), reportFile, file);
                    pySession.predict(pyFile, reportFile, file);
                }
            }
            logger.info("开始运行算法");

        }catch (Exception ex) {
            ae = new AlgorithmException(ex);
            throw ae;
        }finally {
            listener.wasExecuted(context, ae);
            pySession.close();
        }
    }
}
