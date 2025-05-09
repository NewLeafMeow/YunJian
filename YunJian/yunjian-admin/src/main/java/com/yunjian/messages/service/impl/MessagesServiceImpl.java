package com.yunjian.messages.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.messages.mapper.MessagesMapper;
import com.yunjian.messages.domain.Messages;
import com.yunjian.messages.service.IMessagesService;

/**
 * 消息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
@Service
public class MessagesServiceImpl implements IMessagesService 
{
    @Autowired
    private MessagesMapper messagesMapper;

    /**
     * 查询消息
     * 
     * @param id 消息主键
     * @return 消息
     */
    @Override
    public Messages selectMessagesById(Long id)
    {
        return messagesMapper.selectMessagesById(id);
    }

    /**
     * 查询消息列表
     * 
     * @param messages 消息
     * @return 消息
     */
    @Override
    public List<Messages> selectMessagesList(Messages messages)
    {
        return messagesMapper.selectMessagesList(messages);
    }

    /**
     * 新增消息
     * 
     * @param messages 消息
     * @return 结果
     */
    @Override
    public int insertMessages(Messages messages)
    {
        return messagesMapper.insertMessages(messages);
    }

    /**
     * 修改消息
     * 
     * @param messages 消息
     * @return 结果
     */
    @Override
    public int updateMessages(Messages messages)
    {
        return messagesMapper.updateMessages(messages);
    }

    /**
     * 批量删除消息
     * 
     * @param ids 需要删除的消息主键
     * @return 结果
     */
    @Override
    public int deleteMessagesByIds(Long[] ids)
    {
        return messagesMapper.deleteMessagesByIds(ids);
    }

    /**
     * 删除消息信息
     * 
     * @param id 消息主键
     * @return 结果
     */
    @Override
    public int deleteMessagesById(Long id)
    {
        return messagesMapper.deleteMessagesById(id);
    }
}
