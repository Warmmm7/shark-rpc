package rpc.protocol;

import cn.hutool.core.util.IdUtil;
import com.shark.rpc.constant.RpcConstant;
import com.shark.rpc.model.RpcRequest;
import com.shark.rpc.protocol.codec.NettyMessageDecoder;
import com.shark.rpc.protocol.codec.NettyMessageEncoder;
import com.shark.rpc.protocol.common.ProtocolConstant;
import com.shark.rpc.protocol.common.ProtocolMessage;
import com.shark.rpc.protocol.messageEnum.ProtocolMessageSerializerEnum;
import com.shark.rpc.protocol.messageEnum.ProtocolMessageStatusEnum;
import com.shark.rpc.protocol.messageEnum.ProtocolMessageTypeEnum;
import org.junit.Test;

import java.io.IOException;


public class ProtocolMessageTest {
    @Test
    public void testCodec() throws IOException{
        NettyMessageDecoder decoder;
        NettyMessageEncoder encoder;

    }

    private ProtocolMessage getMessage(){
        ProtocolMessage<RpcRequest> message = new ProtocolMessage<>();
        ProtocolMessage.Header header = new ProtocolMessage.Header();
        header.setMagic(ProtocolConstant.PROTOCOL_MAGIC);
        header.setVersion(ProtocolConstant.PROTOCOL_VERSION);
        header.setSerializer((byte)ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte)ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getValue());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("myMethod");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParameterTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"haha","byeBye"});
        message.setHeader(header);
        message.setBody(rpcRequest);
        return message;
    }

}
