package com.yunjian.conversations.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 会话对象 conversations
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
public class Conversations extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 会话ID */
    private Long id;

    /** 会话名称 */
    @Excel(name = "会话名称")
    private String conversationName;

    /** 是否为群聊，0 表示单聊，1 表示群聊 */
    @Excel(name = "是否为群聊，0 表示单聊，1 表示群聊")
    private Integer isGroupChat;

    /** 会话创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "会话创建时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date createdAt;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setConversationName(String conversationName) 
    {
        this.conversationName = conversationName;
    }

    public String getConversationName() 
    {
        return conversationName;
    }
    public void setIsGroupChat(Integer isGroupChat) 
    {
        this.isGroupChat = isGroupChat;
    }

    public Integer getIsGroupChat() 
    {
        return isGroupChat;
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
            .append("conversationName", getConversationName())
            .append("isGroupChat", getIsGroupChat())
            .append("createdAt", getCreatedAt())
            .toString();
    }
}
