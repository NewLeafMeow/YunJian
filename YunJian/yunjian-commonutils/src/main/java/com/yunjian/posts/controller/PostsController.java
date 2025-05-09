package com.yunjian.posts.controller;

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
import com.yunjian.posts.domain.Posts;
import com.yunjian.posts.service.IPostsService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 帖子信息Controller
 * 
 * @author ruoyi
 * @date 2025-05-03
 */
@RestController
@RequestMapping("/posts/posts")
public class PostsController extends BaseController
{
    @Autowired
    private IPostsService postsService;

    /**
     * 查询帖子信息列表
     */
    @PreAuthorize("@ss.hasPermi('posts:posts:list')")
    @GetMapping("/list")
    public TableDataInfo list(Posts posts)
    {
        startPage();
        List<Posts> list = postsService.selectPostsList(posts);
        return getDataTable(list);
    }

    /**
     * 导出帖子信息列表
     */
    @PreAuthorize("@ss.hasPermi('posts:posts:export')")
    @Log(title = "帖子信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Posts posts)
    {
        List<Posts> list = postsService.selectPostsList(posts);
        ExcelUtil<Posts> util = new ExcelUtil<Posts>(Posts.class);
        util.exportExcel(response, list, "帖子信息数据");
    }

    /**
     * 获取帖子信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('posts:posts:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(postsService.selectPostsById(id));
    }

    /**
     * 新增帖子信息
     */
    @PreAuthorize("@ss.hasPermi('posts:posts:add')")
    @Log(title = "帖子信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Posts posts)
    {
        return toAjax(postsService.insertPosts(posts));
    }

    /**
     * 修改帖子信息
     */
    @PreAuthorize("@ss.hasPermi('posts:posts:edit')")
    @Log(title = "帖子信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Posts posts)
    {
        return toAjax(postsService.updatePosts(posts));
    }

    /**
     * 删除帖子信息
     */
    @PreAuthorize("@ss.hasPermi('posts:posts:remove')")
    @Log(title = "帖子信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(postsService.deletePostsByIds(ids));
    }
}
