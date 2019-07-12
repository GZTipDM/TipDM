package com.tipdm.framework.model.dmserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tipdm.framework.model.IdEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by TipDM on 2016/12/9.
 * E-mail:devp@tipdm.com
 */
@Entity
@Table(name = "dm_db_conn")
@JsonIgnoreProperties({"id", "creatorId", "creatorName", /*"isDelete",*/ "lastModifyTime"})
public class DBConnection extends IdEntity<Long> {

    @Column(name = "username", length = 128, nullable = false)
    private String userName;

    @Column(name = "password", length = 128, nullable = false)
    private String password;

    @Column(length = 255, nullable = false)
    private String url;

//    @Column(name = "db_type", nullable = false)
//    private String dbType;

    @Column(name = "is_delete")
    private Boolean isDelete = Boolean.FALSE;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_modify_time")
    private Date lastModifyTime = Calendar.getInstance().getTime();

    @Column(nullable = false)
    @Type(type = "text")
    private String sql;

    public DBConnection(){

    }

    public DBConnection(String userName, String password, String url, String sql){
        this.userName = userName;
        this.password = password;
        this.url = url;
        this.sql = sql;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

//    public String getDbType() {
//        return dbType;
//    }
//
//    public void setDbType(String dbType) {
//        this.dbType = dbType;
//    }

    @JsonIgnore
    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
