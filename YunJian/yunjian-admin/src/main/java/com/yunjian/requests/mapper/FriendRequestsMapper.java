package com.yunjian.requests.mapper;

import java.util.List;
import com.yunjian.requests.domain.FriendRequests;

/**
 * 好友请求Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-20
 */
public interface FriendRequestsMapper 
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
     * 删除好友请求
     * 
     * @param id 好友请求主键
     * @return 结果
     */
    public int deleteFriendRequestsById(Long id);

    /**
     * 批量删除好友请求
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFriendRequestsByIds(Long[] ids);
}
