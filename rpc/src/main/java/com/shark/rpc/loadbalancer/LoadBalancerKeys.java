package com.shark.rpc.loadbalancer;

public interface LoadBalancerKeys {
    /**
     * 轮询方法
     */

    String ROUND_ROBIN = "roundRobin";

    String RANDOM = "random";

    String CONSISTENT_HASH = "consistentHash";
}
