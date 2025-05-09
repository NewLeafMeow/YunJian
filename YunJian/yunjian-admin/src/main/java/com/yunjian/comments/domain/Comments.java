package com.yunjian.comments.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 评论对象 comments
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
public class Comments extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private Long id;

    /** 评论所属帖子ID */
    @Excel(name = "评论所属帖子ID")
    private Long postId;

    /** 评论用户ID */
    @Excel(name = "评论用户ID")
    private Long userId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 父评论ID，NULL表示直接评论 */
    @Excel(name = "父评论ID，NULL表示直接评论")
    private Long parentCommentId;

    /** 评论时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "评论时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPostId(Long postId) 
    {
        this.postId = postId;
    }

    public Long getPostId() 
    {
        return postId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }
    public void setParentCommentId(Long parentCommentId) 
    {
        this.parentCommentId = parentCommentId;
    }

    public Long getParentCommentId() 
    {
        return parentCommentId;
    }
    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("postId", getPostId())
            .append("userId", getUserId())
            .append("content", getContent())
            .append("parentCommentId", getParentCommentId())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
