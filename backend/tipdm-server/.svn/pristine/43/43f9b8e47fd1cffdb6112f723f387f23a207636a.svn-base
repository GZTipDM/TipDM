package com.tipdm.framework.dmserver.core.algo.unparallel.executor;

import com.tipdm.framework.model.dmserver.Component;
import com.tipdm.framework.model.dmserver.ComponentIO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TipDM on 2017/6/21.
 * E-mail:devp@tipdm.com
 */
public class ExecutorContext {

    private Component component;

    private Object session;

    private Map<String, String> inputs;

    private Map<String, String> outputs;

    public ExecutorContext(Component component, Object session){
        this.component = component;
        this.session = session;

        inputs = convertToMap(component.getInputs());
        outputs = convertToMap(component.getOutputs());
    }

    public Component getComponent(){
        return this.component;
    }

    public Object getSession(){
        return this.session;
    }

    public Map<String, String> getInputs(){
        return inputs;
    }

    public Map<String, String> getOutputs(){
        return outputs;
    }

    public Map<String, String> getParams(){
        return component.getParameters();
    }

    private Map<String, String> convertToMap(List<ComponentIO> ioList){
        Map<String, String> entrys = new HashMap<>();
        for(ComponentIO io : ioList) {
            entrys.put(io.getKey(), io.getTempTable());
        }
        return entrys;
    }
}
