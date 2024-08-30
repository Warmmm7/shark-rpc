package com.shark.rpc.protocol.codec;

import com.shark.rpc.model.RpcRequest;
import com.shark.rpc.model.RpcResponse;
import com.shark.rpc.protocol.common.ProtocolConstant;
import com.shark.rpc.protocol.common.ProtocolMessage;
import com.shark.rpc.protocol.messageEnum.ProtocolMessageSerializerEnum;
import com.shark.rpc.protocol.messageEnum.ProtocolMessageTypeEnum;
import com.shark.rpc.serializer.Serializer;
import com.shark.rpc.serializer.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;


import java.util.List;

/**
 * 自定义格式解码
 */
public class NettyMessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        ProtocolMessage msg = new ProtocolMessage();
        ProtocolMessage.Header header = msg.getHeader();
        byte magic = in.getByte(0);
        if (magic != ProtocolConstant.PROTOCOL_MAGIC){
            throw new RuntimeException("消息magic非法");
        }
        header.setMagic(magic);
        header.setVersion(in.getByte(1));
        header.setSerializer(in.getByte(2));
        header.setType(in.getByte(3));
        header.setStatus(in.getByte(4));
        header.setRequestId(in.getLong(5));
        header.setBodyLength(in.getInt(13));//上一个Long是8字节

        msg.setHeader(header);

        //读取指定长度数据 避免粘包
        in.readerIndex(17);//设置读索引到数据部分
        ByteBuf bodyByteBuf = in.readBytes(header.getBodyLength());//body数据
        byte[] bodyBytes = new byte[bodyByteBuf.readableBytes()];
        bodyByteBuf.readBytes(bodyBytes);


        //反序列化消息体
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum
                .getEnumByKey(header.getSerializer());
        if (serializerEnum == null){
            throw new RuntimeException("序列化协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());

        ProtocolMessageTypeEnum typeEnum = ProtocolMessageTypeEnum
                .getEnumByKey(header.getType());
        if (typeEnum == null){
            throw new RuntimeException("序列化消息类型不存在");
        }

        switch (typeEnum){
            case REQUEST:
                RpcRequest request = serializer.deserialize(bodyBytes, RpcRequest.class);
                msg.setBody(request);
                out.add(msg);
            case RESPONSE:
                RpcResponse response = serializer.deserialize(bodyBytes, RpcResponse.class);
                msg.setBody(response);
                out.add(msg);
            case HEART_BEAT:
            case OTHERS:
            default:
                throw new RuntimeException("不支持此消息类型");
        }
    }
}
