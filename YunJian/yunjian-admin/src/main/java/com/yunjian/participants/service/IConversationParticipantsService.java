package com.yunjian.participants.service;

import java.util.List;
import com.yunjian.participants.domain.ConversationParticipants;

/**
 * 会话参与者Service接口
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
public interface IConversationParticipantsService 
{
    /**
     * 查询会话参与者
     * 
     * @param id 会话参与者主键
     * @return 会话参与者
     */
    public ConversationParticipants selectConversationParticipantsById(Long id);

    /**
     * 查询会话参与者列表
     * 
     * @param conversationParticipants 会话参与者
     * @return 会话参与者集合
     */
    public List<ConversationParticipants> selectConversationParticipantsList(ConversationParticipants conversationParticipants);

    /**
     * 新增会话参与者
     * 
     * @param conversationParticipants 会话参与者
     * @return 结果
     */
    public int insertConversationParticipants(ConversationParticipants conversationParticipants);

    /**
     * 修改会话参与者
     * 
     * @param conversationParticipants 会话参与者
     * @return 结果
     */
    public int updateConversationParticipants(ConversationParticipants conversationParticipants);

    /**
     * 批量删除会话参与者
     * 
     * @param ids 需要删除的会话参与者主键集合
     * @return 结果
     */
    public int deleteConversationParticipantsByIds(Long[] ids);

    /**
     * 删除会话参与者信息
     * 
     * @param id 会话参与者主键
     * @return 结果
     */
    public int deleteConversationParticipantsById(Long id);
}
