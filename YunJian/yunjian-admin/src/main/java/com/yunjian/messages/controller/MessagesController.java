package com.yunjian.messages.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yunjian.common.annotation.Log;
import com.yunjian.common.core.controller.BaseController;
import com.yunjian.common.core.domain.AjaxResult;
import com.yunjian.common.enums.BusinessType;
import com.yunjian.messages.domain.Messages;
import com.yunjian.messages.service.IMessagesService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 消息Controller
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
@RestController
@RequestMapping("/messages/messages")
public class MessagesController extends BaseController
{
    @Autowired
    private IMessagesService messagesService;

    /**
     * 查询消息列表
     */
    @PreAuthorize("@ss.hasPermi('messages:messages:list')")
    @GetMapping("/list")
    public TableDataInfo list(Messages messages)
    {
        startPage();
        List<Messages> list = messagesService.selectMessagesList(messages);
        return getDataTable(list);
    }

    /**
     * 导出消息列表
     */
    @PreAuthorize("@ss.hasPermi('messages:messages:export')")
    @Log(title = "消息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Messages messages)
    {
        List<Messages> list = messagesService.selectMessagesList(messages);
        ExcelUtil<Messages> util = new ExcelUtil<Messages>(Messages.class);
        util.exportExcel(response, list, "消息数据");
    }

    /**
     * 获取消息详细信息
     */
    @PreAuthorize("@ss.hasPermi('messages:messages:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(messagesService.selectMessagesById(id));
    }

    /**
     * 新增消息
     */
    @PreAuthorize("@ss.hasPermi('messages:messages:add')")
    @Log(title = "消息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Messages messages)
    {
        return toAjax(messagesService.insertMessages(messages));
    }

    /**
     * 修改消息
     */
    @PreAuthorize("@ss.hasPermi('messages:messages:edit')")
    @Log(title = "消息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Messages messages)
    {
        return toAjax(messagesService.updateMessages(messages));
    }

    /**
     * 删除消息
     */
    @PreAuthorize("@ss.hasPermi('messages:messages:remove')")
    @Log(title = "消息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(messagesService.deleteMessagesByIds(ids));
    }
}
