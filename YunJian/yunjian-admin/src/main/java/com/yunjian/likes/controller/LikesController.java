package com.yunjian.likes.controller;

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
import com.yunjian.likes.domain.Likes;
import com.yunjian.likes.service.ILikesService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 点赞记录Controller
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/likes/likes")
public class LikesController extends BaseController {
    @Autowired
    private ILikesService likesService;

    /**
     * 查询点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('likes:likes:list')")
    @GetMapping("/list")
    public TableDataInfo list(Likes likes) {
        startPage();
        List<Likes> list = likesService.selectLikesList(likes);
        return getDataTable(list);
    }

    /**
     * 导出点赞记录列表
     */
    @PreAuthorize("@ss.hasPermi('likes:likes:export')")
    @Log(title = "点赞记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Likes likes) {
        List<Likes> list = likesService.selectLikesList(likes);
        ExcelUtil<Likes> util = new ExcelUtil<Likes>(Likes.class);
        util.exportExcel(response, list, "点赞记录数据");
    }

    /**
     * 获取点赞记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('likes:likes:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(likesService.selectLikesById(id));
    }

    /**
     * 新增点赞记录
     */
    @PreAuthorize("@ss.hasPermi('likes:likes:add')")
    @Log(title = "点赞记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Likes likes) {
        System.out.println("新增点赞:" + likes);
        return toAjax(likesService.insertLikes(likes));
    }

    /**
     * 修改点赞记录
     */
    @PreAuthorize("@ss.hasPermi('likes:likes:edit')")
    @Log(title = "点赞记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Likes likes) {
        return toAjax(likesService.updateLikes(likes));
    }

    /**
     * 删除点赞记录
     */
    @PreAuthorize("@ss.hasPermi('likes:likes:remove')")
    @Log(title = "点赞记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(likesService.deleteLikesByIds(ids));
    }
}
