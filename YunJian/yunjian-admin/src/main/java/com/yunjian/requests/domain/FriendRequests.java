package com.yunjian.requests.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 好友请求对象 friend_requests
 * 
 * @author ruoyi
 * @date 2025-04-20
 */
public class FriendRequests extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 发起请求的用户ID */
    @Excel(name = "发起请求的用户ID")
    private Long requesterId;

    /** 接收请求的用户ID */
    @Excel(name = "接收请求的用户ID")
    private Long receiverId;

    /** 请求状态（pending、accepted、rejected、blocked） */
    @Excel(name = "请求状态", readConverterExp = "p=ending、accepted、rejected、blocked")
    private String status;

    /** 附加消息（可选） */
    @Excel(name = "附加消息", readConverterExp = "可=选")
    private String message;

    /** 请求创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "请求创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    /** 状态更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "状态更新时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date updatedAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setRequesterId(Long requesterId) 
    {
        this.requesterId = requesterId;
    }

    public Long getRequesterId() 
    {
        return requesterId;
    }
    public void setReceiverId(Long receiverId) 
    {
        this.receiverId = receiverId;
    }

    public Long getReceiverId() 
    {
        return receiverId;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setMessage(String message) 
    {
        this.message = message;
    }

    public String getMessage() 
    {
        return message;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("requesterId", getRequesterId())
            .append("receiverId", getReceiverId())
            .append("status", getStatus())
            .append("message", getMessage())
            .append("createdAt", getCreatedAt())
            .append("updatedAt", getUpdatedAt())
            .toString();
    }
}
