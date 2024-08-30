package com.shark.rpc.fault.retry;



public interface RetryStrategyKeys {
    /**
     * 不充实
     */
    String NO = "no";

    /**
     * 间隔重试
     */
    String FIXED_INTERVAL = "fixedInterval";
}
