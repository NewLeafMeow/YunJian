package com.yunjian.participants.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.participants.mapper.ConversationParticipantsMapper;
import com.yunjian.participants.domain.ConversationParticipants;
import com.yunjian.participants.service.IConversationParticipantsService;

/**
 * 会话参与者Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
@Service
public class ConversationParticipantsServiceImpl implements IConversationParticipantsService 
{
    @Autowired
    private ConversationParticipantsMapper conversationParticipantsMapper;

    /**
     * 查询会话参与者
     * 
     * @param id 会话参与者主键
     * @return 会话参与者
     */
    @Override
    public ConversationParticipants selectConversationParticipantsById(Long id)
    {
        return conversationParticipantsMapper.selectConversationParticipantsById(id);
    }

    /**
     * 查询会话参与者列表
     * 
     * @param conversationParticipants 会话参与者
     * @return 会话参与者
     */
    @Override
    public List<ConversationParticipants> selectConversationParticipantsList(ConversationParticipants conversationParticipants)
    {
        return conversationParticipantsMapper.selectConversationParticipantsList(conversationParticipants);
    }

    /**
     * 新增会话参与者
     * 
     * @param conversationParticipants 会话参与者
     * @return 结果
     */
    @Override
    public int insertConversationParticipants(ConversationParticipants conversationParticipants)
    {
        return conversationParticipantsMapper.insertConversationParticipants(conversationParticipants);
    }

    /**
     * 修改会话参与者
     * 
     * @param conversationParticipants 会话参与者
     * @return 结果
     */
    @Override
    public int updateConversationParticipants(ConversationParticipants conversationParticipants)
    {
        return conversationParticipantsMapper.updateConversationParticipants(conversationParticipants);
    }

    /**
     * 批量删除会话参与者
     * 
     * @param ids 需要删除的会话参与者主键
     * @return 结果
     */
    @Override
    public int deleteConversationParticipantsByIds(Long[] ids)
    {
        return conversationParticipantsMapper.deleteConversationParticipantsByIds(ids);
    }

    /**
     * 删除会话参与者信息
     * 
     * @param id 会话参与者主键
     * @return 结果
     */
    @Override
    public int deleteConversationParticipantsById(Long id)
    {
        return conversationParticipantsMapper.deleteConversationParticipantsById(id);
    }
}
