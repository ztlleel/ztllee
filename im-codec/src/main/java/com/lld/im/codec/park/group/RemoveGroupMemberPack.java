package com.lld.im.codec.park.group;

import lombok.Data;

/**
 * @author: Chackylee
 * @description: 踢人出群通知报文
 **/
@Data
public class RemoveGroupMemberPack {

    private String groupId;

    private String member;

}
