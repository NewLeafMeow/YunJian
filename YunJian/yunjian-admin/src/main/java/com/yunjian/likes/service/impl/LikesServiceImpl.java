package com.yunjian.likes.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.likes.mapper.LikesMapper;
import com.yunjian.likes.domain.Likes;
import com.yunjian.likes.service.ILikesService;

/**
 * 点赞记录Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
@Service
public class LikesServiceImpl implements ILikesService 
{
    @Autowired
    private LikesMapper likesMapper;

    /**
     * 查询点赞记录
     * 
     * @param id 点赞记录主键
     * @return 点赞记录
     */
    @Override
    public Likes selectLikesById(Long id)
    {
        return likesMapper.selectLikesById(id);
    }

    /**
     * 查询点赞记录列表
     * 
     * @param likes 点赞记录
     * @return 点赞记录
     */
    @Override
    public List<Likes> selectLikesList(Likes likes)
    {
        return likesMapper.selectLikesList(likes);
    }

    /**
     * 新增点赞记录
     * 
     * @param likes 点赞记录
     * @return 结果
     */
    @Override
    public int insertLikes(Likes likes)
    {
        return likesMapper.insertLikes(likes);
    }

    /**
     * 修改点赞记录
     * 
     * @param likes 点赞记录
     * @return 结果
     */
    @Override
    public int updateLikes(Likes likes)
    {
        return likesMapper.updateLikes(likes);
    }

    /**
     * 批量删除点赞记录
     * 
     * @param ids 需要删除的点赞记录主键
     * @return 结果
     */
    @Override
    public int deleteLikesByIds(Long[] ids)
    {
        return likesMapper.deleteLikesByIds(ids);
    }

    /**
     * 删除点赞记录信息
     * 
     * @param id 点赞记录主键
     * @return 结果
     */
    @Override
    public int deleteLikesById(Long id)
    {
        return likesMapper.deleteLikesById(id);
    }
}
