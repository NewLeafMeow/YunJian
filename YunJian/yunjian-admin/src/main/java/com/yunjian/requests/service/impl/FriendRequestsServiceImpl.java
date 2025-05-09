package com.yunjian.requests.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.requests.mapper.FriendRequestsMapper;
import com.yunjian.requests.domain.FriendRequests;
import com.yunjian.requests.service.IFriendRequestsService;

/**
 * 好友请求Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-20
 */
@Service
public class FriendRequestsServiceImpl implements IFriendRequestsService 
{
    @Autowired
    private FriendRequestsMapper friendRequestsMapper;

    /**
     * 查询好友请求
     * 
     * @param id 好友请求主键
     * @return 好友请求
     */
    @Override
    public FriendRequests selectFriendRequestsById(Long id)
    {
        return friendRequestsMapper.selectFriendRequestsById(id);
    }

    /**
     * 查询好友请求列表
     * 
     * @param friendRequests 好友请求
     * @return 好友请求
     */
    @Override
    public List<FriendRequests> selectFriendRequestsList(FriendRequests friendRequests)
    {
        return friendRequestsMapper.selectFriendRequestsList(friendRequests);
    }

    /**
     * 新增好友请求
     * 
     * @param friendRequests 好友请求
     * @return 结果
     */
    @Override
    public int insertFriendRequests(FriendRequests friendRequests)
    {
        return friendRequestsMapper.insertFriendRequests(friendRequests);
    }

    /**
     * 修改好友请求
     * 
     * @param friendRequests 好友请求
     * @return 结果
     */
    @Override
    public int updateFriendRequests(FriendRequests friendRequests)
    {
        return friendRequestsMapper.updateFriendRequests(friendRequests);
    }

    /**
     * 批量删除好友请求
     * 
     * @param ids 需要删除的好友请求主键
     * @return 结果
     */
    @Override
    public int deleteFriendRequestsByIds(Long[] ids)
    {
        return friendRequestsMapper.deleteFriendRequestsByIds(ids);
    }

    /**
     * 删除好友请求信息
     * 
     * @param id 好友请求主键
     * @return 结果
     */
    @Override
    public int deleteFriendRequestsById(Long id)
    {
        return friendRequestsMapper.deleteFriendRequestsById(id);
    }
}
