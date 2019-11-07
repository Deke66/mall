package com.deke.mall.task;

import com.deke.mall.common.ParamKeys;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;


import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Slf4j
public class ApplicationThreadPoolTaskExecutor extends ThreadPoolTaskExecutor {

    @Override
    public void execute(Runnable task) {
        super.execute(newRunnable(task));
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(newRunnable(task));
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return super.submit(newCallable(task));
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(Callable<T> task){
        return super.submitListenable(newCallable(task));
    }

    @Override
    public ListenableFuture<?> submitListenable(Runnable task){
        return super.submitListenable(newRunnable(task));
    }


    /**
     * 异步任务执行同步父子线程的MDC
     * @param task
     * @return
     */
    private Callable newCallable(Callable task){
        final Map<String, String> context = MDC.getCopyOfContextMap();
        return ()->{
            MDC.setContextMap(context);
            try{
                return task.call();
            }finally {
                MDC.clear();
            }
        };
    }

    /**
     * 异步任务执行同步父子线程的MDC
     * @param task
     * @return
     */
    private Runnable newRunnable(Runnable task){
        final Map<String, String> context = MDC.getCopyOfContextMap();
        return ()->{
            MDC.setContextMap(context);
            log.info("task traceNo is {}",MDC.get(ParamKeys.TRACE_NO));
            try{
                task.run();
            }finally {
                MDC.clear();
            }
        };
    }


}
