package com.tipdm.framework.model.dmserver;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tipdm.framework.model.IdEntity;
import com.tipdm.framework.persist.datatype.JsonType;
import com.tipdm.framework.persist.datatype.TextType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

/**
 * Created by TipDM on 2016/12/9.
 * E-mail:devp@tipdm.com
 * 组件的输入输出
 */
@Entity
@Table(name = "dm_component_io")
@TypeDefs( {@TypeDef( name= "json", typeClass = JsonType.class), @TypeDef( name= "text", typeClass = TextType.class)})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ComponentIO extends IdEntity<Long> {

    private static final long serialVersionUID = 3740157320487344099L;

    @Column(length = 32, nullable = false)
    private String key;//

    @Column(name = "io_type", nullable = false)
    @Enumerated
    private IOType type = IOType.INPUT;

    @Column(name = "cat")
    @Enumerated
    private Category cat = Category.DATA;

    @Deprecated
    @Column(name = "is_model")
    private Boolean isModel = Boolean.FALSE;

    @Column(name = "can_preview")
    private Boolean canPreview = Boolean.FALSE;

    @Column(name = "preview_mode")
    private PreViewMode preViewMode = PreViewMode.ONLY100;

    @Column()
    @Type(type = "text")
    private String description;

    @Transient
    private String nodeClientId;//输入/输出点的客户端Id

    @Transient
    private String tempTable;

    @Column
    @Enumerated
    private MetaDataAccess access = MetaDataAccess.SAME;

    @Column
    @Type(type = "json")
    private String columns = "[]";

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public IOType getType() {
        return type;
    }

    public void setType(IOType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTempTable() {
        return tempTable;
    }

    public void setTempTable(String tempTable) {
        this.tempTable = tempTable;
    }

    public String getNodeClientId() {
        return nodeClientId;
    }

    public void setNodeClientId(String nodeClientId) {
        this.nodeClientId = nodeClientId;
    }

    public enum IOType{
        INPUT,OUTPUT;
    }

    public enum Category{
        DATA,//数据（存储在数据库中）
        MODEL,//模型文件
        UNSTRUCT;//非结构化文件
    }

    public enum MetaDataAccess{
        SAME,
        APPEND,
        UNKNOWN
    }

    public Boolean getModel() {
        return isModel;
    }

    public void setModel(Boolean model) {
        isModel = model;
    }

    public Boolean getCanPreview() {
        return canPreview;
    }

    public void setCanPreview(Boolean canPreview) {
        this.canPreview = canPreview;
    }

    public Category getCat() {
        if(this.isModel == true){
            cat = Category.MODEL;
        }
        return cat;
    }

    public void setCat(Category cat) {
        this.cat = cat;
    }

    public PreViewMode getPreViewMode() {
        if(this.preViewMode == null){
            this.preViewMode = PreViewMode.ONLY100;
        }
        return preViewMode;
    }

    public void setPreViewMode(PreViewMode preViewMode) {
        this.preViewMode = preViewMode;
    }

    public MetaDataAccess getAccess() {
        if(this.type == IOType.INPUT){
            return null;
        } else{
            if(this.access == null){
                this.access = MetaDataAccess.SAME;
            }
        }
        return access;
    }

    public void setAccess(MetaDataAccess access) {
        this.access = access;
    }

    public String getColumns() {
        if(this.type == IOType.INPUT){
            return null;
        }
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }
}
