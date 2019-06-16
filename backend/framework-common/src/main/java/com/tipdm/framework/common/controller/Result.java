package com.tipdm.framework.common.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by zhoulong on 2016/12/19.
 * E-mail:zhoulong8513@gmail.com
 */
@JsonSerialize()
//@ApiModel
public class Result implements Serializable{

//    @ApiModelProperty(value = "描述信息", required = true, example = "操作成功", position = 0)
    private String message = "操作成功";

    @JsonInclude(JsonInclude.Include.NON_NULL)
//    @ApiModelProperty(value = "返回数据", position = 2)
    private Object data;

//    @ApiModelProperty(value = "状态", required = true, position = 1)
    private Status status = Status.SUCCESS;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status{
        SUCCESS, FAIL
    }
}
