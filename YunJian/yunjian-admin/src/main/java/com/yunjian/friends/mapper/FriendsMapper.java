package com.yunjian.friends.mapper;

import java.util.List;
import com.yunjian.friends.domain.Friends;

/**
 * 好友关系Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-22
 */
public interface FriendsMapper 
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
     * 删除好友关系
     * 
     * @param id 好友关系主键
     * @return 结果
     */
    public int deleteFriendsById(Long id);

    /**
     * 批量删除好友关系
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFriendsByIds(Long[] ids);
}
