package org.springframework.scheduling.quartz;


import com.tipdm.framework.dmserver.core.algo.log.LogAppender;
import com.tipdm.framework.dmserver.core.scheduling.State;
import com.tipdm.framework.dmserver.rpc.MessageManager;
import com.tipdm.framework.dmserver.utils.Constants;
import com.tipdm.framework.dmserver.utils.RedissonUtils;
import com.tipdm.framework.dmserver.websocket.dto.WorkFlowMessage;
import com.tipdm.framework.service.dmserver.MessageService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.SchedulerConfigException;
import org.quartz.core.JobRunShell;
import org.quartz.impl.JobExecutionContextImpl;
import org.quartz.spi.ThreadPool;
import org.redisson.api.RMapCache;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/**
 * Created by TipDM on 2018/1/8.
 * E-mail:devp@tipdm.com
 */
public class LocalTaskExecutorThreadPool implements ThreadPool {

    /**
     * Logger available to subclasses
     */
    protected final Log logger = LogFactory.getLog(getClass());

    private Executor taskExecutor;


    @Override
    public void setInstanceId(String schedInstId) {
    }

    @Override
    public void setInstanceName(String schedName) {
    }


    @Override
    public void initialize() throws SchedulerConfigException {
        // Absolutely needs thread-bound TaskExecutor to initialize.
        this.taskExecutor = SchedulerFactoryBean.getConfigTimeTaskExecutor();
        if (this.taskExecutor == null) {
            throw new SchedulerConfigException(
                    "No local TaskExecutor found for configuration - " +
                            "'taskExecutor' property must be set on SchedulerFactoryBean");
        }
    }

    @Override
    public void shutdown(boolean waitForJobsToComplete) {
    }

    @Override
    public int getPoolSize() {
        return -1;
    }


    @Override
    public boolean runInThread(Runnable runnable) {
        if (runnable == null) {
            return false;
        }

        JobRunShell shell = (JobRunShell) runnable;
        Field field = ReflectionUtils.findField(JobRunShell.class, "jec");
        ReflectionUtils.makeAccessible(field);
        JobExecutionContextImpl jobExecutionContext = (JobExecutionContextImpl) ReflectionUtils.getField(field, shell);
        JobKey jobKey = jobExecutionContext.getJobDetail().getKey();
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        try {
            this.taskExecutor.execute(runnable);
            return true;
        } catch (RejectedExecutionException ex) {
            logger.error("Task has been rejected by TaskExecutor");
            WorkFlowMessage message = new WorkFlowMessage();
            message.setNodeId(jobKey.getName());
            message.setState(State.ERROR);

            String accessToken = jobDataMap.getString("accessToken");
            RMapCache<String, Map<String, String>> mapCache = RedissonUtils.getRMapCache(Constants.WS_CLIENTS);
            Map<String, String> mapping = mapCache.get(accessToken);
            String sessionId = mapping.get("sessionId");
            String rpcAddress = mapping.get("rpcAddress");
            MessageService messageService = MessageManager.getService(rpcAddress);
            messageService.notifyWorkFlowExecStatus(jobKey.getGroup(), sessionId, message);
            LogAppender.error(jobKey.getName(), "队列已达到上限，任务提交失败，请联系管理员");
            return false;
        }
    }

    @Override
    public int blockForAvailableThreads() {
        // The present implementation always returns 1, making Quartz
        // always schedule any tasks that it feels like scheduling.
        // This could be made smarter for specific TaskExecutors,
        // for example calling {@code getMaximumPoolSize() - getActiveCount()}
        // on a {@code java.util.concurrent.ThreadPoolExecutor}.
        return 1;
    }

}
