package com.yunjian.requests.controller;

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
import com.yunjian.requests.domain.FriendRequests;
import com.yunjian.requests.service.IFriendRequestsService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 好友请求Controller
 * 
 * @author ruoyi
 * @date 2025-04-20
 */
@RestController
@RequestMapping("/requests/requests")
public class FriendRequestsController extends BaseController
{
    @Autowired
    private IFriendRequestsService friendRequestsService;

    /**
     * 查询好友请求列表
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:list')")
    @GetMapping("/list")
    public TableDataInfo list(FriendRequests friendRequests)
    {
        startPage();
        List<FriendRequests> list = friendRequestsService.selectFriendRequestsList(friendRequests);
        return getDataTable(list);
    }

    /**
     * 导出好友请求列表
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:export')")
    @Log(title = "好友请求", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, FriendRequests friendRequests)
    {
        List<FriendRequests> list = friendRequestsService.selectFriendRequestsList(friendRequests);
        ExcelUtil<FriendRequests> util = new ExcelUtil<FriendRequests>(FriendRequests.class);
        util.exportExcel(response, list, "好友请求数据");
    }

    /**
     * 获取好友请求详细信息
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(friendRequestsService.selectFriendRequestsById(id));
    }

    /**
     * 新增好友请求
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:add')")
    @Log(title = "好友请求", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FriendRequests friendRequests)
    {
        return toAjax(friendRequestsService.insertFriendRequests(friendRequests));
    }

    /**
     * 修改好友请求
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:edit')")
    @Log(title = "好友请求", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody FriendRequests friendRequests)
    {
        return toAjax(friendRequestsService.updateFriendRequests(friendRequests));
    }

    /**
     * 删除好友请求
     */
    @PreAuthorize("@ss.hasPermi('requests:requests:remove')")
    @Log(title = "好友请求", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(friendRequestsService.deleteFriendRequestsByIds(ids));
    }
}
