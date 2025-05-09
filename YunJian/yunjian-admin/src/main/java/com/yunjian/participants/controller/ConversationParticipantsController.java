package com.yunjian.participants.controller;

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
import com.yunjian.participants.domain.ConversationParticipants;
import com.yunjian.participants.service.IConversationParticipantsService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 会话参与者Controller
 * 
 * @author ruoyi
 * @date 2025-04-23
 */
@RestController
@RequestMapping("/participants/participants")
public class ConversationParticipantsController extends BaseController {
    @Autowired
    private IConversationParticipantsService conversationParticipantsService;

    /**
     * 查询会话参与者列表
     */
    @PreAuthorize("@ss.hasPermi('participants:participants:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConversationParticipants conversationParticipants) {
        startPage();
        List<ConversationParticipants> list = conversationParticipantsService
                .selectConversationParticipantsList(conversationParticipants);
        return getDataTable(list);
    }

    /**
     * 导出会话参与者列表
     */
    @PreAuthorize("@ss.hasPermi('participants:participants:export')")
    @Log(title = "会话参与者", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ConversationParticipants conversationParticipants) {
        List<ConversationParticipants> list = conversationParticipantsService
                .selectConversationParticipantsList(conversationParticipants);
        ExcelUtil<ConversationParticipants> util = new ExcelUtil<ConversationParticipants>(
                ConversationParticipants.class);
        util.exportExcel(response, list, "会话参与者数据");
    }

    /**
     * 获取会话参与者详细信息
     */
    @PreAuthorize("@ss.hasPermi('participants:participants:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(conversationParticipantsService.selectConversationParticipantsById(id));
    }

    /**
     * 新增会话参与者
     */
    @PreAuthorize("@ss.hasPermi('participants:participants:add')")
    @Log(title = "会话参与者", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ConversationParticipants conversationParticipants) {
        return toAjax(conversationParticipantsService.insertConversationParticipants(conversationParticipants));
    }

    /**
     * 修改会话参与者
     */
    @PreAuthorize("@ss.hasPermi('participants:participants:edit')")
    @Log(title = "会话参与者", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ConversationParticipants conversationParticipants) {
        return toAjax(conversationParticipantsService.updateConversationParticipants(conversationParticipants));
    }

    /**
     * 删除会话参与者
     */
    @PreAuthorize("@ss.hasPermi('participants:participants:remove')")
    @Log(title = "会话参与者", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(conversationParticipantsService.deleteConversationParticipantsByIds(ids));
    }
}
