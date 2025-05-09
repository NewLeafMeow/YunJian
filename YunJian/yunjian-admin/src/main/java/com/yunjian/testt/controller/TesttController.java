package com.yunjian.testt.controller;

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
import com.yunjian.testt.domain.Testt;
import com.yunjian.testt.service.ITesttService;
import com.yunjian.common.utils.poi.ExcelUtil;
import com.yunjian.common.core.page.TableDataInfo;

/**
 * 测试Controller
 * 
 * @author ruoyi
 * @date 2025-04-12
 */
@RestController
@RequestMapping("/testt/testt")
public class TesttController extends BaseController
{
    @Autowired
    private ITesttService testtService;

    /**
     * 查询测试列表
     */
    @PreAuthorize("@ss.hasPermi('testt:testt:list')")
    @GetMapping("/list")
    public TableDataInfo list(Testt testt)
    {
        startPage();
        List<Testt> list = testtService.selectTesttList(testt);
        return getDataTable(list);
    }

    /**
     * 导出测试列表
     */
    @PreAuthorize("@ss.hasPermi('testt:testt:export')")
    @Log(title = "测试", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Testt testt)
    {
        List<Testt> list = testtService.selectTesttList(testt);
        ExcelUtil<Testt> util = new ExcelUtil<Testt>(Testt.class);
        util.exportExcel(response, list, "测试数据");
    }

    /**
     * 获取测试详细信息
     */
    @PreAuthorize("@ss.hasPermi('testt:testt:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(testtService.selectTesttById(id));
    }

    /**
     * 新增测试
     */
    @PreAuthorize("@ss.hasPermi('testt:testt:add')")
    @Log(title = "测试", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Testt testt)
    {
        return toAjax(testtService.insertTestt(testt));
    }

    /**
     * 修改测试
     */
    @PreAuthorize("@ss.hasPermi('testt:testt:edit')")
    @Log(title = "测试", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Testt testt)
    {
        return toAjax(testtService.updateTestt(testt));
    }

    /**
     * 删除测试
     */
    @PreAuthorize("@ss.hasPermi('testt:testt:remove')")
    @Log(title = "测试", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(testtService.deleteTesttByIds(ids));
    }
}
