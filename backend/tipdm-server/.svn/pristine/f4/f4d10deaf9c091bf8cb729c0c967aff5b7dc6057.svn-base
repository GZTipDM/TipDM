package com.tipdm.framework.controller.dmserver.dto.datasource;

import com.tipdm.framework.controller.dmserver.dto.DataColumn;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * Created by TipDM on 2017/7/21.
 * E-mail:devp@tipdm.com
 */
//@ApiModel
public class HDFS {

//    @ApiModelProperty(value = "数据表名称", position = 0)
    private String tableName;

//    @ApiModelProperty(value = "表字段信息集合", position = 1)
    private List<DataColumn> columns;

//    @ApiModelProperty(value = "保存时长,单位：天，最长可保存180天", allowableValues = "range[1,180]", position = 2)
    private Integer duration=180;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<DataColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<DataColumn> columns) {
        this.columns = columns;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
