package com.tipdm.framework.dmserver.core.algo.unparallel.predict;

import com.tipdm.framework.common.utils.RedisUtils;
import com.tipdm.framework.common.utils.SpringUtils;
import com.tipdm.framework.dmserver.core.algo.unparallel.AbstractPythonAlgorithm;
import com.tipdm.framework.dmserver.core.algo.unparallel.executor.AlgorithmExecutor;
import com.tipdm.framework.dmserver.core.algo.unparallel.executor.PyExecutor;
import com.tipdm.framework.dmserver.exception.AlgorithmException;
import com.tipdm.framework.dmserver.utils.Constants;
import com.tipdm.framework.model.dmserver.Component;
import com.tipdm.framework.model.dmserver.ComponentIO;
import com.tipdm.framework.model.dmserver.Model;
import com.tipdm.framework.model.dmserver.Step;
import com.tipdm.framework.service.dmserver.ModelService;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 单机算法通用预测器
 * 输入：
 * 一个训练好的模型；
 * 预测数据
 * 输出：
 * 预测结果
 */
public class Predictor extends AbstractPythonAlgorithm {

    private final ModelService modelService = SpringUtils.getBean("modelService", ModelService.class);

    @Override
    protected void execute() throws AlgorithmException {

        try {
            //获取模型的保存目录
            String modelDir = RedisUtils.get(Constants.MODEL_DIR, String.class);
            Map<String, ComponentIO> inputMap = component.getInputs().stream().collect(Collectors.toMap(ComponentIO::getKey, x -> x));
            ComponentIO io = inputMap.get("model");
            //未保存的模型文件以节点ID命名
            String modelName = io.getTempTable();
            Model model = (Model) RedisUtils.getFromMap(Constants.UN_SAVED_MODEL, modelName);
            if (null == model) {
                model = modelService.findOne(Long.parseLong(io.getTempTable()));
            }

            if(null == model){
                throw new AlgorithmException("model file not found");
            }
            File modelFile = new File(modelDir, model.getModelPath());

            if(!modelFile.exists()){
                throw new AlgorithmException("model file not found");
            }

            Component modelComponent = componentService.findOne(model.getComponentId());
            component.setScript(modelComponent.getScript());
            AlgorithmExecutor executor = new PyExecutor(pySession, component, Step.PREDICT);
            executor.run();


        } catch (Exception e) {
            throw new AlgorithmException(e);
        }
    }
}
