package com.yunjian.friends.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 好友关系对象 friends
 * 
 * @author ruoyi
 * @date 2025-04-22
 */
public class Friends extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 唯一标识ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 好友ID */
    @Excel(name = "好友ID")
    private Long friendId;

    /** 好友添加时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "好友添加时间", width = 30, dateFormat = "yyyy-MM-dd")
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
    public void setFriendId(Long friendId) 
    {
        this.friendId = friendId;
    }

    public Long getFriendId() 
    {
        return friendId;
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
            .append("friendId", getFriendId())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
