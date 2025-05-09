package com.yunjian.posts.mapper;

import java.util.List;
import com.yunjian.posts.domain.Posts;

/**
 * 帖子信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-03
 */
public interface PostsMapper 
{
    /**
     * 查询帖子信息
     * 
     * @param id 帖子信息主键
     * @return 帖子信息
     */
    public Posts selectPostsById(Long id);

    /**
     * 查询帖子信息列表
     * 
     * @param posts 帖子信息
     * @return 帖子信息集合
     */
    public List<Posts> selectPostsList(Posts posts);

    /**
     * 新增帖子信息
     * 
     * @param posts 帖子信息
     * @return 结果
     */
    public int insertPosts(Posts posts);

    /**
     * 修改帖子信息
     * 
     * @param posts 帖子信息
     * @return 结果
     */
    public int updatePosts(Posts posts);

    /**
     * 删除帖子信息
     * 
     * @param id 帖子信息主键
     * @return 结果
     */
    public int deletePostsById(Long id);

    /**
     * 批量删除帖子信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deletePostsByIds(Long[] ids);
}
