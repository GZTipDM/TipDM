package com.tipdm.framework.controller.dmserver.dto;

//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;

/**
 * Created by TipDM on 2017/4/27.
 * E-mail:devp@tipdm.com
 */
//@ApiModel(description = "数据库连接信息")
public class RestfulDataSource {

//    @ApiModelProperty(name = "url", notes = "数据库连接URL", required = true, position = 1)
    private String url;

//    @ApiModelProperty(name = "user", notes = "用户名", required = true, position = 2)
    private String user;

//    @ApiModelProperty(name = "password", notes = "密码", required = true, position = 3)
    private String password;

//    @ApiModelProperty(name = "sql", notes = "预测数据抽取SQL", required = true, position = 4)
    private String sql;

//    @ApiModelProperty(name = "target", notes = "预测数据写入的目标表", required = true, position = 5)
    private String target;

//    @ApiModelProperty(name = "truncate", notes = "写入前是否清空表数据", required = true, position = 6)
    private Boolean truncate = Boolean.FALSE;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Boolean getTruncate() {
        return truncate;
    }

    public void setTruncate(Boolean truncate) {
        this.truncate = truncate;
    }
}
