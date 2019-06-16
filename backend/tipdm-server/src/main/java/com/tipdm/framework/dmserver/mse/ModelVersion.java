package com.tipdm.framework.dmserver.mse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelVersion implements Serializable {

    private static final long serialVersionUID = -461933143742235856L;

    private Integer latest = 0;//最新版本

    private List<Integer> versions = new ArrayList<>();//所有版本

    public ModelVersion(){
        increment();
    }

    public Integer getLatest() {
        return latest;
    }

    public void setLatest(Integer latest) {
        this.latest = latest;
    }

    public List<Integer> getVersions() {
        return versions;
    }

    public void setVersions(List<Integer> versions) {
        this.versions = versions;
    }

    public void increment(){
        Integer version = latest + 1;
        versions.add(version);
        latest = version;
    }
}
