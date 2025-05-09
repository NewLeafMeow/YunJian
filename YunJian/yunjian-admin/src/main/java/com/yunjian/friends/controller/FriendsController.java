package com.yunjian.friends.controller;

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
import com.yunjian.friends.domain.Friends;
import com.yunjian.friends.service.IFriendsService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 好友关系Controller
 * 
 * @author ruoyi
 * @date 2025-04-22
 */
@RestController
@RequestMapping("/friends/friends")
public class FriendsController extends BaseController
{
    @Autowired
    private IFriendsService friendsService;

    /**
     * 查询好友关系列表
     */
    @PreAuthorize("@ss.hasPermi('friends:friends:list')")
    @GetMapping("/list")
    public TableDataInfo list(Friends friends)
    {
        startPage();
        List<Friends> list = friendsService.selectFriendsList(friends);
        return getDataTable(list);
    }

    /**
     * 导出好友关系列表
     */
    @PreAuthorize("@ss.hasPermi('friends:friends:export')")
    @Log(title = "好友关系", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Friends friends)
    {
        List<Friends> list = friendsService.selectFriendsList(friends);
        ExcelUtil<Friends> util = new ExcelUtil<Friends>(Friends.class);
        util.exportExcel(response, list, "好友关系数据");
    }

    /**
     * 获取好友关系详细信息
     */
    @PreAuthorize("@ss.hasPermi('friends:friends:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(friendsService.selectFriendsById(id));
    }

    /**
     * 新增好友关系
     */
    @PreAuthorize("@ss.hasPermi('friends:friends:add')")
    @Log(title = "好友关系", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Friends friends)
    {
        return toAjax(friendsService.insertFriends(friends));
    }

    /**
     * 修改好友关系
     */
    @PreAuthorize("@ss.hasPermi('friends:friends:edit')")
    @Log(title = "好友关系", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Friends friends)
    {
        return toAjax(friendsService.updateFriends(friends));
    }

    /**
     * 删除好友关系
     */
    @PreAuthorize("@ss.hasPermi('friends:friends:remove')")
    @Log(title = "好友关系", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(friendsService.deleteFriendsByIds(ids));
    }
}
