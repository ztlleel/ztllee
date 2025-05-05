package com.lld.im.codec;

import com.alibaba.fastjson.JSONObject;
import com.lld.im.codec.proto.Message;
import com.lld.im.codec.proto.MessageHeader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

//消息解码类
public class MessageDecoder extends ByteToMessageDecoder {
    //请求方法+版本号+端表示（标识端类型）+请求体解析方式（json、protobuf）+appId+imei号+请求体长度
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 28) {
            return;
        }
        int command = in.readInt();
        int version = in.readInt();
        int clientType = in.readInt();
        int messageType = in.readInt();
        int appId = in.readInt();
        int imeiLength = in.readInt();
        int bodyLen = in.readInt();

        if (in.readableBytes() < bodyLen + imeiLength) {
            in.resetReaderIndex();
            return;
        }
        byte[] imeiData = new byte[imeiLength];
        in.readBytes(imeiData);
        String imei = new String(imeiData);

        byte[] bodyData = new byte[bodyLen];
        in.readBytes(bodyData);

        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setAppId(appId);
        messageHeader.setClientType(clientType);
        messageHeader.setCommand(command);
        messageHeader.setLength(bodyLen);
        messageHeader.setVersion(version);
        messageHeader.setMessageType(messageType);
        messageHeader.setImei(imei);

        Message messge = new Message();

        if (messageType == 0X0) {
            String body = new String(bodyData);
            JSONObject parse = (JSONObject) JSONObject.parse(body);
            messge.setMessagePack(parse);
        }
        messge.setMessageHeader(messageHeader);

        in.markReaderIndex();
        out.add(messge);
    }
}

