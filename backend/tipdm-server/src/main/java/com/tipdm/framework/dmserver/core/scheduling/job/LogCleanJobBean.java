package com.tipdm.framework.dmserver.core.scheduling.job;

import com.tipdm.framework.common.utils.FileKit;
import com.tipdm.framework.common.utils.PropertiesUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.MDC;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by TipDM on 2018/5/27.
 * 定期清理日志文件
 */
public class LogCleanJobBean extends QuartzJobBean {

    private final static long WEEK = 1000L * 60 * 60 * 24 * 7;

    private static File LOG_DIR;

    static {
        String logHome = PropertiesUtil.getValue("sysconfig/system.properties", "LOG_HOME");

        Assert.notNull(logHome, "未设置日志存储目录");
        LOG_DIR = new File(logHome);
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        long currentTime = System.currentTimeMillis();
        FileKit.listFiles(LOG_DIR, new String[]{"log"}, true).stream().forEach(x -> {
            if (x.isFile()) {
                BasicFileAttributes attr = null;
                try {
                    attr = Files.readAttributes(x.toPath(), BasicFileAttributes.class);
                    long lastAccessTime = attr.lastAccessTime().toMillis();
                    if (currentTime - lastAccessTime >= WEEK) {
                        FileKit.forceDelete(x);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        MDC.clear();
    }
}
