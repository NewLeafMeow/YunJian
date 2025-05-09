package com.yunjian.conversations.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.conversations.mapper.ConversationsMapper;
import com.yunjian.conversations.domain.Conversations;
import com.yunjian.conversations.service.IConversationsService;

/**
 * 会话Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
@Service
public class ConversationsServiceImpl implements IConversationsService 
{
    @Autowired
    private ConversationsMapper conversationsMapper;

    /**
     * 查询会话
     * 
     * @param id 会话主键
     * @return 会话
     */
    @Override
    public Conversations selectConversationsById(Long id)
    {
        return conversationsMapper.selectConversationsById(id);
    }

    /**
     * 查询会话列表
     * 
     * @param conversations 会话
     * @return 会话
     */
    @Override
    public List<Conversations> selectConversationsList(Conversations conversations)
    {
        return conversationsMapper.selectConversationsList(conversations);
    }

    /**
     * 新增会话
     * 
     * @param conversations 会话
     * @return 结果
     */
    @Override
    public int insertConversations(Conversations conversations)
    {
        return conversationsMapper.insertConversations(conversations);
    }

    /**
     * 修改会话
     * 
     * @param conversations 会话
     * @return 结果
     */
    @Override
    public int updateConversations(Conversations conversations)
    {
        return conversationsMapper.updateConversations(conversations);
    }

    /**
     * 批量删除会话
     * 
     * @param ids 需要删除的会话主键
     * @return 结果
     */
    @Override
    public int deleteConversationsByIds(Long[] ids)
    {
        return conversationsMapper.deleteConversationsByIds(ids);
    }

    /**
     * 删除会话信息
     * 
     * @param id 会话主键
     * @return 结果
     */
    @Override
    public int deleteConversationsById(Long id)
    {
        return conversationsMapper.deleteConversationsById(id);
    }
}
