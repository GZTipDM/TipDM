package com.tipdm.framework.dmserver.core.scheduling;

import com.tipdm.framework.common.utils.PropertiesUtil;
import com.tipdm.framework.common.utils.RedisUtils;
import org.apache.commons.lang3.ThreadUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by TipDM on 2017/1/10.
 * E-mail:devp@tipdm.com
 */
public class WorkFlowScheduler {

    private final static Logger logger = LoggerFactory.getLogger(WorkFlowScheduler.class);

    private static long timeout;

    private static Semaphore semaphore;

    //初始化信号池
    static {
        timeout = Long.valueOf(PropertiesUtil.getValue("sysconfig/quartz.properties", "quartz.timeout", "30")) * 60;
        int size = Integer.valueOf(PropertiesUtil.getValue("sysconfig/quartz.properties", "quartz.task.size", "30"));
        semaphore = new Semaphore(size);
        logger.info("workFlow schedule timeout: {}", timeout);
    }

    private Scheduler scheduler;

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    /**
     * 运行流程
     *
     * @param flow
     * @return 流程ID
     */
    public String execute(WorkFlow flow) throws SchedulerException, IllegalAccessException {

        if (null == flow) {
            throw new SchedulerException("workFlow can not be null");
        }

        try {
            //获取流程执行信号锁
            boolean acquire = semaphore.tryAcquire(1, TimeUnit.MINUTES);
            if(!acquire){
                throw new IllegalAccessException("服务器暂时无法处理您的请求，请稍后再试！");
            }
            String workFlowId = flow.getId();
            //停止流程（如果在运行）
            shutdownWorkFlow(flow.getId());
            HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
            String accessToken = request.getHeader("accessToken");
            WorkFlowDexecutor dexecutor = new WorkFlowDexecutor(accessToken, flow, scheduler);
            //启动一个线程监听流程
            WorkFlowHandler workFlowHandler = new WorkFlowHandler(workFlowId, scheduler, dexecutor, semaphore);
            workFlowHandler.start();
            RedisUtils.putToMap("executing-flows", workFlowId, workFlowHandler.getId(), 12L);
            return workFlowId;
        } catch (InterruptedException e) {
            throw new IllegalAccessException("服务器暂时无法处理您的请求，请稍后再试！");
        }
    }

    /**
     * 停止流程
     *
     * @param workFlowId 流程ID
     * @throws SchedulerException
     */
    public boolean shutdownWorkFlow(String workFlowId) throws SchedulerException {
        Long threadId = (Long)RedisUtils.getFromMap("executing-flows", workFlowId);
        if(threadId != null) {
            WorkFlowHandler workFlowHandler = (WorkFlowHandler) ThreadUtils.findThreadById(threadId);
            if (null != workFlowHandler) {
                workFlowHandler.abort();
            }
        }
        return true;
    }

}
