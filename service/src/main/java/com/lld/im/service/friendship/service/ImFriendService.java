package com.lld.im.service.friendship.service;

import com.lld.im.common.ResponseVO;
import com.lld.im.common.model.RequestBase;
import com.lld.im.common.model.SyncReq;
import com.lld.im.service.friendship.model.rep.AddFriendReq;
import com.lld.im.service.friendship.model.rep.ImporFriendShipReq;
import com.lld.im.service.friendship.model.rep.UpdateFriendReq;
//import com.lld.im.common.model.SyncReq;
//import com.lld.im.service.friendship.model.req.*;

import java.util.List;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
public interface ImFriendService {

    public ResponseVO importFriendShip(ImporFriendShipReq req);

    public ResponseVO addFriend(AddFriendReq req);

    public ResponseVO updateFriend(UpdateFriendReq req);


    public ResponseVO deleteFriend(com.lld.im.service.friendship.model.req.DeleteFriendReq req);

    public ResponseVO deleteAllFriend(com.lld.im.service.friendship.model.req.DeleteFriendReq req);

    public ResponseVO getAllFriendShip(com.lld.im.service.friendship.model.req.GetAllFriendShipReq req);

    public ResponseVO getRelation(com.lld.im.service.friendship.model.req.GetRelationReq req);

    public ResponseVO doAddFriend(RequestBase requestBase, String fromId, com.lld.im.service.friendship.model.req.FriendDto dto, Integer appId);

    public ResponseVO checkFriendship(com.lld.im.service.friendship.model.req.CheckFriendShipReq req);

    public ResponseVO addBlack(com.lld.im.service.friendship.model.req.AddFriendShipBlackReq req);

    public ResponseVO deleteBlack(com.lld.im.service.friendship.model.req.DeleteBlackReq req);

    public ResponseVO checkBlack(com.lld.im.service.friendship.model.req.CheckFriendShipReq req);

    public ResponseVO syncFriendshipList(SyncReq req);

    public List<String> getAllFriendId(String userId, Integer appId);

}
