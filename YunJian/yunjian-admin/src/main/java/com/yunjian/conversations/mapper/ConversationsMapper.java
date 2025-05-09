package com.yunjian.conversations.mapper;

import java.util.List;
import com.yunjian.conversations.domain.Conversations;

/**
 * 会话Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
public interface ConversationsMapper 
{
    /**
     * 查询会话
     * 
     * @param id 会话主键
     * @return 会话
     */
    public Conversations selectConversationsById(Long id);

    /**
     * 查询会话列表
     * 
     * @param conversations 会话
     * @return 会话集合
     */
    public List<Conversations> selectConversationsList(Conversations conversations);

    /**
     * 新增会话
     * 
     * @param conversations 会话
     * @return 结果
     */
    public int insertConversations(Conversations conversations);

    /**
     * 修改会话
     * 
     * @param conversations 会话
     * @return 结果
     */
    public int updateConversations(Conversations conversations);

    /**
     * 删除会话
     * 
     * @param id 会话主键
     * @return 结果
     */
    public int deleteConversationsById(Long id);

    /**
     * 批量删除会话
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteConversationsByIds(Long[] ids);
}
