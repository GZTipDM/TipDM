package com.tipdm.framework.controller.dmserver.dto;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

/**
 * Created by TipDM on 2016/12/29.
 * E-mail:devp@tipdm.com
 */
//@ApiModel(value = "cat")
public class Doc {

//    @ApiModelProperty(position = 1, value = "名称")
    private String name;

//    @ApiModelProperty(position = 2, value = "父级ID")
    private Long parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
