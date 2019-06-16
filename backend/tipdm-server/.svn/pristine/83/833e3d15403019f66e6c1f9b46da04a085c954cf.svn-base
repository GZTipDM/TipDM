package org.dmg.pmml;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

@JsonInclude (
        value = JsonInclude.Include.NON_EMPTY
)
public class Field implements Serializable {

    private String id = null;

    private String name = null;

    private DataType dataType = null;

    private OpType opType = null;

    private List<String> values = null;


    public Field(){
    }

    public Field(String id){
        setId(id);
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }

    public String getId(){
        return this.id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public DataType getDataType(){
        return this.dataType;
    }

    public void setDataType(DataType dataType){
        this.dataType = dataType;
    }

    public OpType getOpType(){
        return this.opType;
    }

    public void setOpType(OpType opType){
        this.opType = opType;
    }

    public List<String> getValues(){
        return this.values;
    }

    public void setValues(List<String> values){
        this.values = values;
    }
}
