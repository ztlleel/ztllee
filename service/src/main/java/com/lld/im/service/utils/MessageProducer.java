package com.lld.im.service.utils;

import com.alibaba.fastjson.JSONObject;
import com.lld.im.codec.proto.MessagePack;
import com.lld.im.common.Constants.Constants;
import com.lld.im.common.enums.command.Command;
import com.lld.im.common.model.ClientInfo;
import com.lld.im.common.model.UserSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class MessageProducer {
    private static Logger logger = LoggerFactory.getLogger(ZKit.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    UserSessionUtils userSessionUtils;

    private String queueName = Constants.RabbitConstants.MessageService2Im;
    public boolean sendMessage(UserSession session, Object message) {
        try {
            logger.info("send message == " + message);
            rabbitTemplate.convertAndSend(queueName, session.getBrokerId() + "", message);
            return true;
        } catch (Exception e) {
            logger.info("send error:" + e.getMessage());
            return false;
        }
    }

    //包装数据，调用sendmessage
    public boolean sendPack(String toId, Command command, Object msg, UserSession userSession) {
        MessagePack messagePack = new MessagePack();
        messagePack.setCommand(command.getCommand());
        messagePack.setToId(toId);
        messagePack.setClientType(userSession.getClientType());
        messagePack.setAppId(userSession.getAppId());
        messagePack.setImei(userSession.getImei());
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(msg));
        messagePack.setData(jsonObject);
        String s = JSONObject.toJSONString(messagePack);
        return sendMessage(userSession, s);
    }
    //发送给所有端的方法
    public List<ClientInfo>  sendToUser(String toId,Command command,Object data,Integer appId){
        List<UserSession> userSession
                = userSessionUtils.getUserSession(appId, toId);
        List<ClientInfo> list = new ArrayList<>();
        for (UserSession session : userSession) {
            boolean b = sendPack(toId, command, data, session);
            if(b){
                list.add(new ClientInfo(session.getAppId(),session.getClientType(),session.getImei()));
            }
        }
        return list;
    }

    public void sendToUser(String toId, Integer clientType,String imei, Command command,
                           Object data, Integer appId){
        if(clientType != null && StringUtils.isNotBlank(imei)){
            ClientInfo clientInfo = new ClientInfo(appId, clientType, imei);
            sendToUserExceptClient(toId,command,data,clientInfo);
        }else{
            sendToUser(toId,command,data,appId);
        }
    }

    //发送给某个用户的指定客户端
    public void sendToUser(String toId, Command command
            , Object data, ClientInfo clientInfo){
        UserSession userSession = userSessionUtils.getUserSession(clientInfo.getAppId(), toId, clientInfo.getClientType(),
                clientInfo.getImei());
        sendPack(toId,command,data,userSession);
    }

    private boolean isMatch(UserSession sessionDto, ClientInfo clientInfo) {
        return Objects.equals(sessionDto.getAppId(), clientInfo.getAppId())
                && Objects.equals(sessionDto.getImei(), clientInfo.getImei())
                && Objects.equals(sessionDto.getClientType(), clientInfo.getClientType());
    }

    //发送给除了某一端的其他端
    public void sendToUserExceptClient(String toId, Command command
            , Object data, ClientInfo clientInfo){
        List<UserSession> userSession = userSessionUtils
                .getUserSession(clientInfo.getAppId(),
                        toId);
        for (UserSession session : userSession) {
            if(!isMatch(session,clientInfo)){
                sendPack(toId,command,data,session);
            }
        }
    }
}
