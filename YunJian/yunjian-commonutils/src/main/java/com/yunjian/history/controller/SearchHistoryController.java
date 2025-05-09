package com.yunjian.history.controller;

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
import com.yunjian.history.domain.SearchHistory;
import com.yunjian.history.service.ISearchHistoryService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 搜索历史Controller
 * 
 * @author ruoyi
 * @date 2025-05-02
 */
@RestController
@RequestMapping("/history/history")
public class SearchHistoryController extends BaseController
{
    @Autowired
    private ISearchHistoryService searchHistoryService;

    /**
     * 查询搜索历史列表
     */
    @PreAuthorize("@ss.hasPermi('history:history:list')")
    @GetMapping("/list")
    public TableDataInfo list(SearchHistory searchHistory)
    {
        startPage();
        List<SearchHistory> list = searchHistoryService.selectSearchHistoryList(searchHistory);
        return getDataTable(list);
    }

    /**
     * 导出搜索历史列表
     */
    @PreAuthorize("@ss.hasPermi('history:history:export')")
    @Log(title = "搜索历史", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SearchHistory searchHistory)
    {
        List<SearchHistory> list = searchHistoryService.selectSearchHistoryList(searchHistory);
        ExcelUtil<SearchHistory> util = new ExcelUtil<SearchHistory>(SearchHistory.class);
        util.exportExcel(response, list, "搜索历史数据");
    }

    /**
     * 获取搜索历史详细信息
     */
    @PreAuthorize("@ss.hasPermi('history:history:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(searchHistoryService.selectSearchHistoryById(id));
    }

    /**
     * 新增搜索历史
     */
    @PreAuthorize("@ss.hasPermi('history:history:add')")
    @Log(title = "搜索历史", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SearchHistory searchHistory)
    {
        return toAjax(searchHistoryService.insertSearchHistory(searchHistory));
    }

    /**
     * 修改搜索历史
     */
    @PreAuthorize("@ss.hasPermi('history:history:edit')")
    @Log(title = "搜索历史", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SearchHistory searchHistory)
    {
        return toAjax(searchHistoryService.updateSearchHistory(searchHistory));
    }

    /**
     * 删除搜索历史
     */
    @PreAuthorize("@ss.hasPermi('history:history:remove')")
    @Log(title = "搜索历史", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(searchHistoryService.deleteSearchHistoryByIds(ids));
    }
}
