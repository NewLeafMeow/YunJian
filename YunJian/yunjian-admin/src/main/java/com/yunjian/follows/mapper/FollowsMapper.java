package com.yunjian.follows.mapper;

import java.util.List;
import com.yunjian.follows.domain.Follows;

/**
 * 关注关系Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
public interface FollowsMapper 
{
    /**
     * 查询关注关系
     * 
     * @param followerId 关注关系主键
     * @return 关注关系
     */
    public Follows selectFollowsByFollowerId(Long followerId);

    /**
     * 查询关注关系列表
     * 
     * @param follows 关注关系
     * @return 关注关系集合
     */
    public List<Follows> selectFollowsList(Follows follows);

    /**
     * 新增关注关系
     * 
     * @param follows 关注关系
     * @return 结果
     */
    public int insertFollows(Follows follows);

    /**
     * 修改关注关系
     * 
     * @param follows 关注关系
     * @return 结果
     */
    public int updateFollows(Follows follows);

    /**
     * 删除关注关系
     * 
     * @param followerId 关注关系主键
     * @return 结果
     */
    public int deleteFollowsByFollowerId(Long followerId);

    /**
     * 批量删除关注关系
     * 
     * @param followerIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteFollowsByFollowerIds(Long[] followerIds);
}
