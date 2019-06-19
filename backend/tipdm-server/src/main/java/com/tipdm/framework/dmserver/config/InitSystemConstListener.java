package com.tipdm.framework.dmserver.config;

import com.tipdm.framework.common.utils.PropertiesUtil;
import com.tipdm.framework.common.utils.RedisUtils;
import com.tipdm.framework.dmserver.utils.Constants;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.util.Map;

/**
 * Created by TipDM on 2017/2/24.
 * E-mail:devp@tipdm.com
 * 初始化系统常量
 */
@WebListener
public class InitSystemConstListener implements ServletContextListener {

    private String doc;

    private String model;

    private String report;

    private String jar;


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        Map<String, String> map = PropertiesUtil.getProperties("sysconfig/system.properties");

        String rootDir = map.get("ROOT_DIR");

        doc = map.get("document.dir");
        model = map.get("model.dir");
        report = map.get("report.dir");
        jar = map.get("jar.dir");

        //检测流程存放目录是否存在
        existsDir(rootDir, doc);
        //检测模型存放目录是否存在
        existsDir(rootDir, model);

        existsDir(rootDir, report);

        existsDir(rootDir, jar);

        //检测公共目录
        existsDir(rootDir, Constants.SMB_PUBLIC_DIR);
        //检测数据源存储目录
        existsDir(rootDir, Constants.SMB_PRIVATE_DIR);
    }

    /**
     * 检测目录是否存在
     */
    private void existsDir(String rootDir, String dir) {

        File file = new File(rootDir, dir);
        if(!file.exists()){
            file.mkdirs();
        }
        RedisUtils.set(dir, file.getAbsolutePath());
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
