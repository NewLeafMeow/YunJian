package com.yunjian.requests.service;

import java.util.List;
import com.yunjian.requests.domain.FriendRequests;

/**
 * 好友请求Service接口
 * 
 * @author ruoyi
 * @date 2025-04-20
 */
public interface IFriendRequestsService 
{
    /**
     * 查询好友请求
     * 
     * @param id 好友请求主键
     * @return 好友请求
     */
    public FriendRequests selectFriendRequestsById(Long id);

    /**
     * 查询好友请求列表
     * 
     * @param friendRequests 好友请求
     * @return 好友请求集合
     */
    public List<FriendRequests> selectFriendRequestsList(FriendRequests friendRequests);

    /**
     * 新增好友请求
     * 
     * @param friendRequests 好友请求
     * @return 结果
     */
    public int insertFriendRequests(FriendRequests friendRequests);

    /**
     * 修改好友请求
     * 
     * @param friendRequests 好友请求
     * @return 结果
     */
    public int updateFriendRequests(FriendRequests friendRequests);

    /**
     * 批量删除好友请求
     * 
     * @param ids 需要删除的好友请求主键集合
     * @return 结果
     */
    public int deleteFriendRequestsByIds(Long[] ids);

    /**
     * 删除好友请求信息
     * 
     * @param id 好友请求主键
     * @return 结果
     */
    public int deleteFriendRequestsById(Long id);
}
