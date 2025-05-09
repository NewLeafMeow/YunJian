package com.yunjian.participants.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 会话参与者对象 conversation_participants
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
public class ConversationParticipants extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 记录ID */
    private Long id;

    /** 会话ID */
    @Excel(name = "会话ID")
    private Long conversationId;

    /** 参与用户ID */
    @Excel(name = "参与用户ID")
    private Long userId;

    /** 加入时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "加入时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date joinedAt;

    /** 会话参与者身份 */
    @Excel(name = "会话参与者身份")
    private String participantIdentity;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setConversationId(Long conversationId) 
    {
        this.conversationId = conversationId;
    }

    public Long getConversationId() 
    {
        return conversationId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setJoinedAt(Date joinedAt) 
    {
        this.joinedAt = joinedAt;
    }

    public Date getJoinedAt() 
    {
        return joinedAt;
    }
    public void setParticipantIdentity(String participantIdentity) 
    {
        this.participantIdentity = participantIdentity;
    }

    public String getParticipantIdentity() 
    {
        return participantIdentity;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("conversationId", getConversationId())
            .append("userId", getUserId())
            .append("joinedAt", getJoinedAt())
            .append("participantIdentity", getParticipantIdentity())
            .toString();
    }
}
