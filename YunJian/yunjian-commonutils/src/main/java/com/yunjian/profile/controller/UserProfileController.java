package com.yunjian.profile.controller;

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
import com.yunjian.profile.domain.UserProfile;
import com.yunjian.profile.service.IUserProfileService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 用户画像Controller
 * 
 * @author ruoyi
 * @date 2025-05-02
 */
@RestController
@RequestMapping("/profile/profile")
public class UserProfileController extends BaseController
{
    @Autowired
    private IUserProfileService userProfileService;

    /**
     * 查询用户画像列表
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:list')")
    @GetMapping("/list")
    public TableDataInfo list(UserProfile userProfile)
    {
        startPage();
        List<UserProfile> list = userProfileService.selectUserProfileList(userProfile);
        return getDataTable(list);
    }

    /**
     * 导出用户画像列表
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:export')")
    @Log(title = "用户画像", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, UserProfile userProfile)
    {
        List<UserProfile> list = userProfileService.selectUserProfileList(userProfile);
        ExcelUtil<UserProfile> util = new ExcelUtil<UserProfile>(UserProfile.class);
        util.exportExcel(response, list, "用户画像数据");
    }

    /**
     * 获取用户画像详细信息
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(userProfileService.selectUserProfileById(id));
    }

    /**
     * 新增用户画像
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:add')")
    @Log(title = "用户画像", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody UserProfile userProfile)
    {
        return toAjax(userProfileService.insertUserProfile(userProfile));
    }

    /**
     * 修改用户画像
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:edit')")
    @Log(title = "用户画像", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody UserProfile userProfile)
    {
        return toAjax(userProfileService.updateUserProfile(userProfile));
    }

    /**
     * 删除用户画像
     */
    @PreAuthorize("@ss.hasPermi('profile:profile:remove')")
    @Log(title = "用户画像", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(userProfileService.deleteUserProfileByIds(ids));
    }
}
