package com.yunjian.posts.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.posts.mapper.PostsMapper;
import com.yunjian.posts.domain.Posts;
import com.yunjian.posts.service.IPostsService;

/**
 * 帖子信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-03
 */
@Service
public class PostsServiceImpl implements IPostsService 
{
    @Autowired
    private PostsMapper postsMapper;

    /**
     * 查询帖子信息
     * 
     * @param id 帖子信息主键
     * @return 帖子信息
     */
    @Override
    public Posts selectPostsById(Long id)
    {
        return postsMapper.selectPostsById(id);
    }

    /**
     * 查询帖子信息列表
     * 
     * @param posts 帖子信息
     * @return 帖子信息
     */
    @Override
    public List<Posts> selectPostsList(Posts posts)
    {
        return postsMapper.selectPostsList(posts);
    }

    /**
     * 新增帖子信息
     * 
     * @param posts 帖子信息
     * @return 结果
     */
    @Override
    public int insertPosts(Posts posts)
    {
        return postsMapper.insertPosts(posts);
    }

    /**
     * 修改帖子信息
     * 
     * @param posts 帖子信息
     * @return 结果
     */
    @Override
    public int updatePosts(Posts posts)
    {
        return postsMapper.updatePosts(posts);
    }

    /**
     * 批量删除帖子信息
     * 
     * @param ids 需要删除的帖子信息主键
     * @return 结果
     */
    @Override
    public int deletePostsByIds(Long[] ids)
    {
        return postsMapper.deletePostsByIds(ids);
    }

    /**
     * 删除帖子信息信息
     * 
     * @param id 帖子信息主键
     * @return 结果
     */
    @Override
    public int deletePostsById(Long id)
    {
        return postsMapper.deletePostsById(id);
    }
}
