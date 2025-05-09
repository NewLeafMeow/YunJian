package com.yunjian.history.mapper;

import java.util.List;
import com.yunjian.history.domain.SearchHistory;

/**
 * 搜索历史Mapper接口
 * 
 * @author ruoyi
 * @date 2025-05-02
 */
public interface SearchHistoryMapper 
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
     * 删除搜索历史
     * 
     * @param id 搜索历史主键
     * @return 结果
     */
    public int deleteSearchHistoryById(Long id);

    /**
     * 批量删除搜索历史
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSearchHistoryByIds(Long[] ids);
}
