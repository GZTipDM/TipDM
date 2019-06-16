package com.tipdm.framework.controller.dmserver.dto;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

/**
 * Created by TipDM on 2017/1/3.
 * E-mail:devp@tipdm.com
 */
//@ApiModel(value = "算法分类")
public class Category {

//    @ApiModelProperty(position = 1, value = "分类名")
    private String name;

//    @ApiModelProperty(position = 2, value = "父级分类")
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
