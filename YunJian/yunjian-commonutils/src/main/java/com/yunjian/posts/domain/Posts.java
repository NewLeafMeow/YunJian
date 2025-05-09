package com.yunjian.posts.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 帖子信息对象 posts
 * 
 * @author ruoyi
 * @date 2025-05-03
 */
public class Posts extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 帖子ID */
    private Long id;

    /** 发布帖子用户ID */
    @Excel(name = "发布帖子用户ID")
    private Long userId;

    /** 帖子标题 */
    @Excel(name = "帖子标题")
    private String title;

    /** 帖子正文媒体 */
    @Excel(name = "帖子正文媒体")
    private String media;

    /** 发布时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "发布时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 最后更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "最后更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    /** 帖子状态（0正常，1删除） */
    @Excel(name = "帖子状态", readConverterExp = "0=正常，1删除")
    private String status;

    /** 帖子分类 */
    @Excel(name = "帖子分类")
    private String category;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setMedia(String media) 
    {
        this.media = media;
    }

    public String getMedia() 
    {
        return media;
    }
    public void setCreatedAt(Date createdAt) 
    {
        this.createdAt = createdAt;
    }

    public Date getCreatedAt() 
    {
        return createdAt;
    }
    public void setUpdatedAt(Date updatedAt) 
    {
        this.updatedAt = updatedAt;
    }

    public Date getUpdatedAt() 
    {
        return updatedAt;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setCategory(String category) 
    {
        this.category = category;
    }

    public String getCategory() 
    {
        return category;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("title", getTitle())
            .append("media", getMedia())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .append("status", getStatus())
            .append("category", getCategory())
            .toString();
    }
}
