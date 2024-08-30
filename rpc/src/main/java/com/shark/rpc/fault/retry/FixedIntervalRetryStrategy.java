package com.shark.rpc.fault.retry;

import com.github.rholder.retry.*;
import com.shark.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 重试策略 ---- 固定时间间隔
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy{

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws ExecutionException,RetryException {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)//重试异常条件
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))//等待策略 3s重试
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))//三次尝试后停止
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        log.info("重试次数：{}", attempt.getAttemptNumber());
                    }
                })
                .build();
        return retryer.call(callable);

    }
}
