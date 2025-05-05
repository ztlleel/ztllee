package com.lld.im.codec.park.friendship;

import lombok.Data;

/**
 * @author: Chackylee
 * @description: 已读好友申请通知报文
 **/
@Data
public class ReadAllFriendRequestPack {

    private String fromId;

    private Long sequence;
}
