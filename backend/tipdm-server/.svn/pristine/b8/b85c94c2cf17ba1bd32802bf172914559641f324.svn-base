package com.tipdm.framework.dmserver.core.scheduling;

import com.github.dexecutor.core.DefaultDexecutor;
import com.github.dexecutor.core.DexecutorConfig;
import com.github.dexecutor.core.ExecutionConfig;
import com.github.dexecutor.core.support.ThreadPoolUtil;
import com.github.dexecutor.core.task.ExecutionResults;
import org.quartz.Scheduler;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by TipDM on 2019/4/30.
 */
public class WorkFlowDexecutor<T, R> {

    private ExecutorService executorService;

    private DefaultDexecutor dexecutor;

    private AtomicBoolean abort;

    private String accessToken;


    public WorkFlowDexecutor(String accessToken, WorkFlow workFlow, Scheduler scheduler){
        this.executorService = Executors.newFixedThreadPool(ThreadPoolUtil.ioIntesivePoolSize());
        this.abort = new AtomicBoolean(false);
        this.accessToken = accessToken;
        buildGraph(workFlow, scheduler);
    }

    private void  buildGraph(WorkFlow workFlow, Scheduler scheduler){
        Set<Dependency> dependencies = workFlow.getDependencies();
        DexecutorConfig<String, String> config = new DexecutorConfig(this.executorService, new JobProvider(workFlow, scheduler, abort, accessToken));

        this.dexecutor =  new DefaultDexecutor<String, String>(config);

        for(Dependency dependency : dependencies){
            dexecutor.addDependency(dependency.getSource(), dependency.getTarget());
        }
    }


    public ExecutionResults<T, R> execute(ExecutionConfig config){
        return this.dexecutor.execute(config);
    }

    public void shutdown(){
        this.executorService.shutdown();
    }

    public void abort(){
        abort.compareAndSet(false, true);
    }

    public String getAccessToken() {
        return accessToken;
    }
}
