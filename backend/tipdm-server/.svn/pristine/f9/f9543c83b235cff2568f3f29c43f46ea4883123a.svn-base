package com.tipdm.framework.dmserver.core.algo.model;

import com.tipdm.framework.common.utils.PropertiesUtil;
import com.tipdm.framework.common.utils.RedisUtils;
import com.tipdm.framework.common.utils.SpringUtils;
import com.tipdm.framework.dmserver.core.algo.IAlgorithm;
import com.tipdm.framework.dmserver.exception.AlgorithmException;
import com.tipdm.framework.dmserver.utils.Constants;
import com.tipdm.framework.model.dmserver.Component;
import com.tipdm.framework.model.dmserver.ComponentIO;
import com.tipdm.framework.model.dmserver.Model;
import com.tipdm.framework.service.dmserver.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhoulong E-mail:devp@tipdm.com
 * @version 创建时间：2017年11月18日 上午11:53:31
 * 模型载入器
 */
public class ModelLoader implements IAlgorithm {

    private final static Logger logger = LoggerFactory.getLogger(ModelLoader.class);

    private Component component;

    private ModelService modelService = SpringUtils.getBean("modelService", ModelService.class);

    @Override
    public void run(Component component) throws AlgorithmException {
        this.component = component;
        truncateLog();
        Map<String, ComponentIO> outputMap = component.getOutputs().stream().collect(Collectors.toMap(ComponentIO::getKey, x -> x));
        ComponentIO io = outputMap.get("model");
        Model model = modelService.findOne(Long.parseLong(io.getTempTable()));
        if(null == model){
            logger.error("加载模型出错，无法获取模型数据");
            return;
        }
        File file = new File(RedisUtils.get(Constants.MODEL_DIR, String.class), model.getModelPath());
        if(!file.exists()){
            logger.error("加载模型出错，模型文件:{}不存在", model.getModelPath());
            return;
        }
        logger.info("loaded model {}", model.getModelName());
    }

    @SuppressWarnings("all")
    protected void truncateLog(){
        String id = component.getClientId().toString();
        String log_home = PropertiesUtil.getValue("sysconfig/system.properties", "LOG_HOME");
        try {
            PrintWriter writer = new PrintWriter(new File(log_home+"/"+id+".log"));
            writer.print("");
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            logger.error("FileNotFoundException: {}", e.getMessage());
        }
        MDC.put("component", id);
    }
}
