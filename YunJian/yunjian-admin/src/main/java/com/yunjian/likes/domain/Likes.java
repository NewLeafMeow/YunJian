package com.yunjian.likes.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 点赞记录对象 likes
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
public class Likes extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 点赞记录ID */
    private Long id;

    /** 点赞用户ID */
    @Excel(name = "点赞用户ID")
    private Long userId;

    /** 点赞对象类型（post, comment） */
    @Excel(name = "点赞对象类型", readConverterExp = "p=ost,,c=omment")
    private String targetType;

    /** 被点赞对象的ID */
    @Excel(name = "被点赞对象的ID")
    private Long targetId;

    /** 点赞时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "点赞时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

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
    public void setTargetType(String targetType) 
    {
        this.targetType = targetType;
    }

    public String getTargetType() 
    {
        return targetType;
    }
    public void setTargetId(Long targetId) 
    {
        this.targetId = targetId;
    }

    public Long getTargetId() 
    {
        return targetId;
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
            .append("userId", getUserId())
            .append("targetType", getTargetType())
            .append("targetId", getTargetId())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
