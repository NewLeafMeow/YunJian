package com.yunjian.history.service;

import java.util.List;
import com.yunjian.history.domain.SearchHistory;

/**
 * 搜索历史Service接口
 * 
 * @author ruoyi
 * @date 2025-05-02
 */
public interface ISearchHistoryService 
{
    /**
     * 查询搜索历史
     * 
     * @param id 搜索历史主键
     * @return 搜索历史
     */
    public SearchHistory selectSearchHistoryById(Long id);

    /**
     * 查询搜索历史列表
     * 
     * @param searchHistory 搜索历史
     * @return 搜索历史集合
     */
    public List<SearchHistory> selectSearchHistoryList(SearchHistory searchHistory);

    /**
     * 新增搜索历史
     * 
     * @param searchHistory 搜索历史
     * @return 结果
     */
    public int insertSearchHistory(SearchHistory searchHistory);

    /**
     * 修改搜索历史
     * 
     * @param searchHistory 搜索历史
     * @return 结果
     */
    public int updateSearchHistory(SearchHistory searchHistory);

    /**
     * 批量删除搜索历史
     * 
     * @param ids 需要删除的搜索历史主键集合
     * @return 结果
     */
    public int deleteSearchHistoryByIds(Long[] ids);

    /**
     * 删除搜索历史信息
     * 
     * @param id 搜索历史主键
     * @return 结果
     */
    public int deleteSearchHistoryById(Long id);
}
