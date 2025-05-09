package com.yunjian.follows.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.follows.mapper.FollowsMapper;
import com.yunjian.follows.domain.Follows;
import com.yunjian.follows.service.IFollowsService;

/**
 * 关注关系Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
@Service
public class FollowsServiceImpl implements IFollowsService 
{
    @Autowired
    private FollowsMapper followsMapper;

    /**
     * 查询关注关系
     * 
     * @param followerId 关注关系主键
     * @return 关注关系
     */
    @Override
    public Follows selectFollowsByFollowerId(Long followerId)
    {
        return followsMapper.selectFollowsByFollowerId(followerId);
    }

    /**
     * 查询关注关系列表
     * 
     * @param follows 关注关系
     * @return 关注关系
     */
    @Override
    public List<Follows> selectFollowsList(Follows follows)
    {
        return followsMapper.selectFollowsList(follows);
    }

    /**
     * 新增关注关系
     * 
     * @param follows 关注关系
     * @return 结果
     */
    @Override
    public int insertFollows(Follows follows)
    {
        return followsMapper.insertFollows(follows);
    }

    /**
     * 修改关注关系
     * 
     * @param follows 关注关系
     * @return 结果
     */
    @Override
    public int updateFollows(Follows follows)
    {
        return followsMapper.updateFollows(follows);
    }

    /**
     * 批量删除关注关系
     * 
     * @param followerIds 需要删除的关注关系主键
     * @return 结果
     */
    @Override
    public int deleteFollowsByFollowerIds(Long[] followerIds)
    {
        return followsMapper.deleteFollowsByFollowerIds(followerIds);
    }

    /**
     * 删除关注关系信息
     * 
     * @param followerId 关注关系主键
     * @return 结果
     */
    @Override
    public int deleteFollowsByFollowerId(Long followerId)
    {
        return followsMapper.deleteFollowsByFollowerId(followerId);
    }
}
