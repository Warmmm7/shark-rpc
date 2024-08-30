package com.shark.rpc.protocol.codec;

import com.shark.rpc.protocol.common.ProtocolMessage;
import com.shark.rpc.protocol.messageEnum.ProtocolMessageSerializerEnum;
import com.shark.rpc.serializer.Serializer;
import com.shark.rpc.serializer.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 对自定义的格式进行编码
 */
public class NettyMessageEncoder extends MessageToByteEncoder<ProtocolMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, ProtocolMessage msg, ByteBuf sendBuf) throws Exception {
        if (msg == null || msg.getHeader() == null){
            throw new Exception("编码失败，数据信息不完整");
        }
        //向缓冲区写入字节
        ProtocolMessage.Header header = msg.getHeader();
        sendBuf.writeByte(header.getMagic());
        sendBuf.writeByte(header.getVersion());
        sendBuf.writeByte(header.getSerializer());
        sendBuf.writeByte(header.getType());
        sendBuf.writeByte(header.getStatus());
        sendBuf.writeLong(header.getRequestId());

        //获取序列化器
        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum
                .getEnumByKey(header.getSerializer());

        if (serializerEnum == null){
            throw new Exception("序列化协议不存在");
        }
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.getValue());
        byte[] bodyBytes = serializer.serialize(msg.getBody());
        //写入body信息
        sendBuf.writeInt(bodyBytes.length);
        sendBuf.writeBytes(bodyBytes);
    }
}
