package com.yunjian.messages.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 消息对象 messages
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
public class Messages extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 消息ID */
    private Long id;

    /** 所属会话ID */
    @Excel(name = "所属会话ID")
    private Long conversationId;

    /** 发送者ID */
    @Excel(name = "发送者ID")
    private Long senderId;

    /** 消息内容 */
    @Excel(name = "消息内容")
    private String messageContent;

    /** 消息发送时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "消息发送时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date sentAt;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public Long getConversationId() {
        return conversationId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public Date getSentAt() {
        return sentAt;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("conversationId", getConversationId())
                .append("senderId", getSenderId())
                .append("messageContent", getMessageContent())
                .append("sentAt", getSentAt())
                .toString();
    }
}
