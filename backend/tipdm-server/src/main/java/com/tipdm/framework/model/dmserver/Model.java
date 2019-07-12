package com.tipdm.framework.model.dmserver;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tipdm.framework.common.utils.StringKit;
import com.tipdm.framework.dmserver.mse.ModelVersion;
import com.tipdm.framework.model.IdEntity;
import org.hibernate.annotations.Type;
import org.dmg.pmml.Field;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 模型
 */
@Entity
@Table(name = "dm_algorithm_model", uniqueConstraints = {@UniqueConstraint(columnNames = {"node_id", "model_version"})})
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Model extends IdEntity<Long>{

    private static final long serialVersionUID = 5266969562001633740L;

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Column(name = "algorithm_name", nullable = true)
    private String algorithmName;

    @Column()
    @Type(type = "text")
    private String features;

    @Column(name = "target")
    @Type(type = "text")
    private String target;

    @Column(name = "outputs")
    @Type(type = "text")
    private String outputs;

    @Column()
    @Type(type = "text")
    private String params;

    @Column(name = "model_path", nullable = false)
    private String modelPath;

    @Column(name = "report_file")
    private String reportFile;

    @Column
    @Type(type = "text")
    private String info;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deploy_time")
    private Date deployTime = Calendar.getInstance().getTime();

    @Column(name = "model_version")
    private Integer version = 0;

    @Transient
    private ModelVersion versionInfo;

    @Column(name = "node_id")
    private String nodeId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Transient
    private Long parentId;

    @Column(name = "train_model_component_id")
    private Long componentId;//构建该模型所使用算法组件的ID

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelPath() {
        return modelPath;
    }

    public void setModelPath(String modelPath) {
        this.modelPath = modelPath;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public List<Field> getFeatures() {
        return JSON.parseArray(features, Field.class);
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public List<Field> getTarget() {
        return JSON.parseArray(target, Field.class);
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<Field> getOutputs() {
        return JSON.parseArray(outputs, Field.class);
    }

    public void setOutputs(String outputs) {
        this.outputs = outputs;
    }

    public String[] getParams() {

        if(StringKit.isNotBlank(params)){
            return StringKit.split(params, ";");
        }
        return null;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getReportFile() {
        return reportFile;
    }

    public void setReportFile(String reportFile) {
        this.reportFile = reportFile;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Date getDeployTime() {
        return deployTime;
    }

    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ModelVersion getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(ModelVersion versionInfo) {
        this.versionInfo = versionInfo;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }
}
