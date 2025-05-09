package com.yunjian.friends.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.friends.mapper.FriendsMapper;
import com.yunjian.friends.domain.Friends;
import com.yunjian.friends.service.IFriendsService;

/**
 * 好友关系Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-22
 */
@Service
public class FriendsServiceImpl implements IFriendsService 
{
    @Autowired
    private FriendsMapper friendsMapper;

    /**
     * 查询好友关系
     * 
     * @param id 好友关系主键
     * @return 好友关系
     */
    @Override
    public Friends selectFriendsById(Long id)
    {
        return friendsMapper.selectFriendsById(id);
    }

    /**
     * 查询好友关系列表
     * 
     * @param friends 好友关系
     * @return 好友关系
     */
    @Override
    public List<Friends> selectFriendsList(Friends friends)
    {
        return friendsMapper.selectFriendsList(friends);
    }

    /**
     * 新增好友关系
     * 
     * @param friends 好友关系
     * @return 结果
     */
    @Override
    public int insertFriends(Friends friends)
    {
        return friendsMapper.insertFriends(friends);
    }

    /**
     * 修改好友关系
     * 
     * @param friends 好友关系
     * @return 结果
     */
    @Override
    public int updateFriends(Friends friends)
    {
        return friendsMapper.updateFriends(friends);
    }

    /**
     * 批量删除好友关系
     * 
     * @param ids 需要删除的好友关系主键
     * @return 结果
     */
    @Override
    public int deleteFriendsByIds(Long[] ids)
    {
        return friendsMapper.deleteFriendsByIds(ids);
    }

    /**
     * 删除好友关系信息
     * 
     * @param id 好友关系主键
     * @return 结果
     */
    @Override
    public int deleteFriendsById(Long id)
    {
        return friendsMapper.deleteFriendsById(id);
    }
}
