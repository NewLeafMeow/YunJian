package com.yunjian.conversations.controller;

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
import com.yunjian.conversations.domain.Conversations;
import com.yunjian.conversations.service.IConversationsService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 会话Controller
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
@RestController
@RequestMapping("/conversations/conversations")
public class ConversationsController extends BaseController
{
    @Autowired
    private IConversationsService conversationsService;

    /**
     * 查询会话列表
     */
    @PreAuthorize("@ss.hasPermi('conversations:conversations:list')")
    @GetMapping("/list")
    public TableDataInfo list(Conversations conversations)
    {
        startPage();
        List<Conversations> list = conversationsService.selectConversationsList(conversations);
        return getDataTable(list);
    }

    /**
     * 导出会话列表
     */
    @PreAuthorize("@ss.hasPermi('conversations:conversations:export')")
    @Log(title = "会话", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Conversations conversations)
    {
        List<Conversations> list = conversationsService.selectConversationsList(conversations);
        ExcelUtil<Conversations> util = new ExcelUtil<Conversations>(Conversations.class);
        util.exportExcel(response, list, "会话数据");
    }

    /**
     * 获取会话详细信息
     */
    @PreAuthorize("@ss.hasPermi('conversations:conversations:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(conversationsService.selectConversationsById(id));
    }

    /**
     * 新增会话
     */
    @PreAuthorize("@ss.hasPermi('conversations:conversations:add')")
    @Log(title = "会话", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Conversations conversations)
    {
        return toAjax(conversationsService.insertConversations(conversations));
    }

    /**
     * 修改会话
     */
    @PreAuthorize("@ss.hasPermi('conversations:conversations:edit')")
    @Log(title = "会话", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Conversations conversations)
    {
        return toAjax(conversationsService.updateConversations(conversations));
    }

    /**
     * 删除会话
     */
    @PreAuthorize("@ss.hasPermi('conversations:conversations:remove')")
    @Log(title = "会话", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(conversationsService.deleteConversationsByIds(ids));
    }
}
