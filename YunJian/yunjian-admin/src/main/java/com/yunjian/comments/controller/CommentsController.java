package com.yunjian.comments.controller;

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
import com.yunjian.comments.domain.Comments;
import com.yunjian.comments.service.ICommentsService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 评论Controller
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/comments/comments")
public class CommentsController extends BaseController
{
    @Autowired
    private ICommentsService commentsService;

    /**
     * 查询评论列表
     */
    @PreAuthorize("@ss.hasPermi('comments:comments:list')")
    @GetMapping("/list")
    public TableDataInfo list(Comments comments)
    {
        startPage();
        List<Comments> list = commentsService.selectCommentsList(comments);
        return getDataTable(list);
    }

    /**
     * 导出评论列表
     */
    @PreAuthorize("@ss.hasPermi('comments:comments:export')")
    @Log(title = "评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Comments comments)
    {
        List<Comments> list = commentsService.selectCommentsList(comments);
        ExcelUtil<Comments> util = new ExcelUtil<Comments>(Comments.class);
        util.exportExcel(response, list, "评论数据");
    }

    /**
     * 获取评论详细信息
     */
    @PreAuthorize("@ss.hasPermi('comments:comments:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(commentsService.selectCommentsById(id));
    }

    /**
     * 新增评论
     */
    @PreAuthorize("@ss.hasPermi('comments:comments:add')")
    @Log(title = "评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Comments comments)
    {
        return toAjax(commentsService.insertComments(comments));
    }

    /**
     * 修改评论
     */
    @PreAuthorize("@ss.hasPermi('comments:comments:edit')")
    @Log(title = "评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Comments comments)
    {
        return toAjax(commentsService.updateComments(comments));
    }

    /**
     * 删除评论
     */
    @PreAuthorize("@ss.hasPermi('comments:comments:remove')")
    @Log(title = "评论", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(commentsService.deleteCommentsByIds(ids));
    }
}
