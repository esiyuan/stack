package com.analyze.stack.util.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池配置
 *
 * @author
 * @create 2019-09-19-21:22
 */
@Component
@Slf4j
public class ThreadPoolExecutorUtil {

    public static final String All_SIMPLE_EXECUTOR = "allSimpleExecutor";


    private static Map<String, ThreadPoolExecutor> executorServiceMap = new HashMap<>();

    private static class CustomerDiscardPolicy implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.info("线程池容量满了，任务直接抛弃: [{}]", r);
        }
    }


    static {
        executorServiceMap.put(All_SIMPLE_EXECUTOR, new ThreadPoolExecutor(4, 10, 10, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10), new CustomizableThreadFactory(All_SIMPLE_EXECUTOR), new ThreadPoolExecutor.CallerRunsPolicy()));
    }

    @PostConstruct
    public void init() {
        for (ThreadPoolExecutor executorService : executorServiceMap.values()) {
            executorService.allowCoreThreadTimeOut(true);
        }
    }

    @PreDestroy
    public void destroy() {
        for (ExecutorService executorService : executorServiceMap.values()) {
            executorService.shutdownNow();
        }
    }


    public static void execute(Runnable runnable, String executorName) {
        executorServiceMap.get(executorName).execute(new CustomerRunner(MDCUtil.getOrCreateCurrentMdcLogUid()) {
            @Override
            public void run() {
                MDCUtil.setMdcLogUid(ObjectUtils.defaultIfNull(getUid(), StringUtils.EMPTY));
                try {
                    log.info("traceId : {}", MDCUtil.getOrCreateCurrentMdcLogUid());
                    runnable.run();
                } finally {
                    MDCUtil.removeMdcLogUid();
                }
            }
        });
    }

    @AllArgsConstructor
    @Data
    private abstract static class CustomerRunner implements Runnable {
        private String uid;

    }

    @AllArgsConstructor
    @Data
    public abstract static class ObjectParamRunner<T> implements Runnable {
        private T param;

    }


}
