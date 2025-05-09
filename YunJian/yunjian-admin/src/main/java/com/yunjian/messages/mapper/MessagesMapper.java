package com.yunjian.messages.mapper;

import java.util.List;
import com.yunjian.messages.domain.Messages;

/**
 * 消息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
public interface MessagesMapper 
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
     * 删除消息
     * 
     * @param id 消息主键
     * @return 结果
     */
    public int deleteMessagesById(Long id);

    /**
     * 批量删除消息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMessagesByIds(Long[] ids);
}
