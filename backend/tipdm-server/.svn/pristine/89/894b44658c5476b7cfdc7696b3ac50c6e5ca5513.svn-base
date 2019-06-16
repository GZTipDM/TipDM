package com.tipdm.framework.dmserver.core.scheduling.model;

import com.tipdm.framework.model.dmserver.ElementTab;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by TipDM on 2017/1/10.
 * E-mail:devp@tipdm.com
 * 节点
 */
public class Node implements Serializable {

    private String id;

    private Long serverId;

    private String name;

    private String targetAlgorithm;

    private Boolean isSupportPMML;

    private String runStyle;

    private String iconPath;

    private String description;

    private Boolean allowViewSource = false;

    private Integer minimumInput = 1;

    private Integer top;

    private Integer left;

    private List<IO> inputs = new ArrayList<>();

    private List<IO> outputs = new ArrayList<>();

    private List<ElementTab> tabs = new ArrayList<>();

    private Boolean hasReport = Boolean.FALSE;

    private List<Map<String, Object>> inputData = new ArrayList<>();

    private List<Map<String, Object>> outputData = new ArrayList<>();

    private List<Map<String, Object>> outputExtraData = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getServerId() {
        return serverId;
    }

    public void setServerId(Long serverId) {
        this.serverId = serverId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTargetAlgorithm() {
        return targetAlgorithm;
    }

    public void setTargetAlgorithm(String targetAlgorithm) {
        this.targetAlgorithm = targetAlgorithm;
    }

    public List<IO> getInputs() {
        return inputs;
    }

    public void setInputs(List<IO> inputs) {
        this.inputs = inputs;
    }

    public List<IO> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<IO> outputs) {
        this.outputs = outputs;
    }

    public List<ElementTab> getTabs() {
        return tabs;
    }

    public void setTabs(List<ElementTab> tabs) {
        this.tabs = tabs;
    }

    public Boolean getSupportPMML() {
        return isSupportPMML;
    }

    public void setSupportPMML(Boolean supportPMML) {
        isSupportPMML = supportPMML;
    }

    public String getRunStyle() {
        return runStyle;
    }

    public void setRunStyle(String runStyle) {
        this.runStyle = runStyle;
    }

    public Boolean getAllowViewSource() {
        return allowViewSource;
    }

    public void setAllowViewSource(Boolean allowViewSource) {
        this.allowViewSource = allowViewSource;
    }

    public Integer getMinimumInput() {
        return minimumInput;
    }

    public void setMinimumInput(Integer minimumInput) {
        this.minimumInput = minimumInput;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
    }

    public Integer getLeft() {
        return left;
    }

    public void setLeft(Integer left) {
        this.left = left;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getHasReport() {
        return hasReport;
    }

    public void setHasReport(Boolean hasReport) {
        this.hasReport = hasReport;
    }

    public List<Map<String, Object>> getInputData() {
        return inputData;
    }

    public void setInputData(List<Map<String, Object>> inputData) {
        this.inputData = inputData;
    }

    public List<Map<String, Object>> getOutputData() {
        return outputData;
    }

    public void setOutputData(List<Map<String, Object>> outputData) {
        this.outputData = outputData;
    }

    public List<Map<String, Object>> getOutputExtraData() {
        return outputExtraData;
    }

    public void setOutputExtraData(List<Map<String, Object>> outputExtraData) {
        this.outputExtraData = outputExtraData;
    }
}
