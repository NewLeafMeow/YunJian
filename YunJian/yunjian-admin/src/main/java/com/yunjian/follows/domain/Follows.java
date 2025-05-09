package com.yunjian.follows.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 关注关系对象 follows
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
public class Follows extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 关注者ID */
    private Long followerId;

    /** 被关注者ID */
    @Excel(name = "被关注者ID")
    private Long followeeId;

    /** 关注时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "关注时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    public void setFollowerId(Long followerId) 
    {
        this.followerId = followerId;
    }

    public Long getFollowerId() 
    {
        return followerId;
    }
    public void setFolloweeId(Long followeeId) 
    {
        this.followeeId = followeeId;
    }

    public Long getFolloweeId() 
    {
        return followeeId;
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
            .append("followerId", getFollowerId())
            .append("followeeId", getFolloweeId())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
