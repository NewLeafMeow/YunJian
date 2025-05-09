package com.yunjian.likes.service;

import java.util.List;
import com.yunjian.likes.domain.Likes;

/**
 * 点赞记录Service接口
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
public interface ILikesService 
{
    /**
     * 查询点赞记录
     * 
     * @param id 点赞记录主键
     * @return 点赞记录
     */
    public Likes selectLikesById(Long id);

    /**
     * 查询点赞记录列表
     * 
     * @param likes 点赞记录
     * @return 点赞记录集合
     */
    public List<Likes> selectLikesList(Likes likes);

    /**
     * 新增点赞记录
     * 
     * @param likes 点赞记录
     * @return 结果
     */
    public int insertLikes(Likes likes);

    /**
     * 修改点赞记录
     * 
     * @param likes 点赞记录
     * @return 结果
     */
    public int updateLikes(Likes likes);

    /**
     * 批量删除点赞记录
     * 
     * @param ids 需要删除的点赞记录主键集合
     * @return 结果
     */
    public int deleteLikesByIds(Long[] ids);

    /**
     * 删除点赞记录信息
     * 
     * @param id 点赞记录主键
     * @return 结果
     */
    public int deleteLikesById(Long id);
}
