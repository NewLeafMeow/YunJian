package com.yunjian.friends.service;

import java.util.List;
import com.yunjian.friends.domain.Friends;

/**
 * 好友关系Service接口
 * 
 * @author ruoyi
 * @date 2025-04-22
 */
public interface IFriendsService 
{
    /**
     * 查询好友关系
     * 
     * @param id 好友关系主键
     * @return 好友关系
     */
    public Friends selectFriendsById(Long id);

    /**
     * 查询好友关系列表
     * 
     * @param friends 好友关系
     * @return 好友关系集合
     */
    public List<Friends> selectFriendsList(Friends friends);

    /**
     * 新增好友关系
     * 
     * @param friends 好友关系
     * @return 结果
     */
    public int insertFriends(Friends friends);

    /**
     * 修改好友关系
     * 
     * @param friends 好友关系
     * @return 结果
     */
    public int updateFriends(Friends friends);

    /**
     * 批量删除好友关系
     * 
     * @param ids 需要删除的好友关系主键集合
     * @return 结果
     */
    public int deleteFriendsByIds(Long[] ids);

    /**
     * 删除好友关系信息
     * 
     * @param id 好友关系主键
     * @return 结果
     */
    public int deleteFriendsById(Long id);
}
