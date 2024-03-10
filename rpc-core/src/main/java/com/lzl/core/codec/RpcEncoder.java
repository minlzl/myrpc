package com.lzl.core.codec;

import com.lzl.core.protocol.MessageHeader;
import com.lzl.core.protocol.MessageProtocol;
import com.lzl.core.serialization.RpcSerialization;
import com.lzl.core.serialization.MySerializationFactory;
import com.lzl.core.serialization.SerializationTypeEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.StandardCharsets;

public class RpcEncoder<T> extends MessageToByteEncoder<MessageProtocol<T>> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageProtocol<T> messageProtocol, ByteBuf byteBuf) throws Exception {
        MessageHeader header = messageProtocol.getHeader();
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getSerialization());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeCharSequence(header.getRequestId(), StandardCharsets.UTF_8);

        RpcSerialization rpcSerialization = MySerializationFactory.getRpcSerialization(SerializationTypeEnum.parseByType(header.getSerialization()));
        byte[] data = rpcSerialization.serialize(messageProtocol.getBody());

        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}
