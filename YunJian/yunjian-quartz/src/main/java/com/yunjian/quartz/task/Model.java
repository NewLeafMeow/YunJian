package com.yunjian.quartz.task;

import com.github.pagehelper.PageHelper;
import com.yunjian.common.core.page.PageDomain;
import com.yunjian.common.model.ModelPredictionClient;
import com.yunjian.posts.domain.Posts;
import com.yunjian.posts.service.IPostsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class Model {

    @Autowired
    private IPostsService postsService;

    /**
     * 定时任务方法：分页查询并更新帖子信息（如分类）
     *
     * @param pageSize 每页数量
     */
    @Transactional // 启用事务管理，避免多条更新出错
    public void updatePostsWithCategory(Integer pageSize) {
        // 初始化分页参数（当前页固定为第一页）
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(1);
        pageDomain.setPageSize(pageSize);
        pageDomain.setOrderByColumn("updatedAt"); // 按更新时间排序
        pageDomain.setIsAsc("asc");

        // 创建查询条件：只查 category 为空的帖子
        Posts posts = new Posts();
        posts.setCategory("0"); // 设置 category 为 null 表示查询该字段为空的数据

        // 分页查询符合条件的帖子
        PageHelper.startPage(pageDomain.getPageNum(), pageDomain.getPageSize());
        PageHelper.orderBy(pageDomain.getOrderBy());

        List<Posts> postList = postsService.selectPostsList(posts); // 查询 category 为空的帖子

        // 遍历处理每条帖子
        for (Posts post : postList) {
            try {
                String media = post.getMedia();
                System.out.println("待预测的文本：" + media);

                // 调用模型预测接口获取预测类别
                String predictedCategory = ModelPredictionClient.predictTextCategory(media);
                System.out.println("预测类型标签：" + predictedCategory);

                // 更新帖子的分类信息
                post.setCategory(predictedCategory);

                // 保存更新后的帖子信息
                postsService.updatePosts(post); // 假设你的 service 提供了此方法
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 清除 PageHelper 线程变量
        PageHelper.clearPage();
    }
}