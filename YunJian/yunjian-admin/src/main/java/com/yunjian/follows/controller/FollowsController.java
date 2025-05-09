package com.yunjian.follows.controller;

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
import com.yunjian.follows.domain.Follows;
import com.yunjian.follows.service.IFollowsService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 关注关系Controller
 * 
 * @author ruoyi
 * @date 2025-02-28
 */
@RestController
@RequestMapping("/follows/follows")
public class FollowsController extends BaseController
{
    @Autowired
    private IFollowsService followsService;

    /**
     * 查询关注关系列表
     */
    @PreAuthorize("@ss.hasPermi('follows:follows:list')")
    @GetMapping("/list")
    public TableDataInfo list(Follows follows)
    {
        startPage();
        List<Follows> list = followsService.selectFollowsList(follows);
        return getDataTable(list);
    }

    /**
     * 导出关注关系列表
     */
    @PreAuthorize("@ss.hasPermi('follows:follows:export')")
    @Log(title = "关注关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Follows follows)
    {
        List<Follows> list = followsService.selectFollowsList(follows);
        ExcelUtil<Follows> util = new ExcelUtil<Follows>(Follows.class);
        util.exportExcel(response, list, "关注关系数据");
    }

    /**
     * 获取关注关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('follows:follows:query')")
    @GetMapping(value = "/{followerId}")
    public AjaxResult getInfo(@PathVariable("followerId") Long followerId)
    {
        return success(followsService.selectFollowsByFollowerId(followerId));
    }

    /**
     * 新增关注关系
     */
    @PreAuthorize("@ss.hasPermi('follows:follows:add')")
    @Log(title = "关注关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Follows follows)
    {
        return toAjax(followsService.insertFollows(follows));
    }

    /**
     * 修改关注关系
     */
    @PreAuthorize("@ss.hasPermi('follows:follows:edit')")
    @Log(title = "关注关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Follows follows)
    {
        return toAjax(followsService.updateFollows(follows));
    }

    /**
     * 删除关注关系
     */
    @PreAuthorize("@ss.hasPermi('follows:follows:remove')")
    @Log(title = "关注关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{followerIds}")
    public AjaxResult remove(@PathVariable Long[] followerIds)
    {
        return toAjax(followsService.deleteFollowsByFollowerIds(followerIds));
    }
}
