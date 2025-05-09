package com.yunjian.messages.service;

import java.util.List;
import com.yunjian.messages.domain.Messages;

/**
 * 消息Service接口
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
public interface IMessagesService 
{
    /**
     * 查询消息
     * 
     * @param id 消息主键
     * @return 消息
     */
    public Messages selectMessagesById(Long id);

    /**
     * 查询消息列表
     * 
     * @param messages 消息
     * @return 消息集合
     */
    public List<Messages> selectMessagesList(Messages messages);

    /**
     * 新增消息
     * 
     * @param messages 消息
     * @return 结果
     */
    public int insertMessages(Messages messages);

    /**
     * 修改消息
     * 
     * @param messages 消息
     * @return 结果
     */
    public int updateMessages(Messages messages);

    /**
     * 批量删除消息
     * 
     * @param ids 需要删除的消息主键集合
     * @return 结果
     */
    public int deleteMessagesByIds(Long[] ids);

    /**
     * 删除消息信息
     * 
     * @param id 消息主键
     * @return 结果
     */
    public int deleteMessagesById(Long id);
}
