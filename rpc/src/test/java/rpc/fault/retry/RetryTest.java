package rpc.fault.retry;

import com.shark.rpc.fault.retry.NoRetryStrategy;
import com.shark.rpc.fault.retry.RetryStrategy;
import com.shark.rpc.model.RpcResponse;
import org.junit.Test;

public class RetryTest {
    RetryStrategy retryStrategy = new NoRetryStrategy();

    @Test
    public void doRetry(){
        try {
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("重试");
                throw new RuntimeException("模拟重试失败");
            });
            System.out.println(rpcResponse);
        }catch (Exception e){
            System.out.println("重试失败...");
            e.printStackTrace();
        }

    }
}
