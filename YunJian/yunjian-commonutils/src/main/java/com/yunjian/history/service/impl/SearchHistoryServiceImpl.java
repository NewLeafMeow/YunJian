package com.yunjian.history.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.history.mapper.SearchHistoryMapper;
import com.yunjian.history.domain.SearchHistory;
import com.yunjian.history.service.ISearchHistoryService;

/**
 * 搜索历史Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-02
 */
@Service
public class SearchHistoryServiceImpl implements ISearchHistoryService 
{
    @Autowired
    private SearchHistoryMapper searchHistoryMapper;

    /**
     * 查询搜索历史
     * 
     * @param id 搜索历史主键
     * @return 搜索历史
     */
    @Override
    public SearchHistory selectSearchHistoryById(Long id)
    {
        return searchHistoryMapper.selectSearchHistoryById(id);
    }

    /**
     * 查询搜索历史列表
     * 
     * @param searchHistory 搜索历史
     * @return 搜索历史
     */
    @Override
    public List<SearchHistory> selectSearchHistoryList(SearchHistory searchHistory)
    {
        return searchHistoryMapper.selectSearchHistoryList(searchHistory);
    }

    /**
     * 新增搜索历史
     * 
     * @param searchHistory 搜索历史
     * @return 结果
     */
    @Override
    public int insertSearchHistory(SearchHistory searchHistory)
    {
        return searchHistoryMapper.insertSearchHistory(searchHistory);
    }

    /**
     * 修改搜索历史
     * 
     * @param searchHistory 搜索历史
     * @return 结果
     */
    @Override
    public int updateSearchHistory(SearchHistory searchHistory)
    {
        return searchHistoryMapper.updateSearchHistory(searchHistory);
    }

    /**
     * 批量删除搜索历史
     * 
     * @param ids 需要删除的搜索历史主键
     * @return 结果
     */
    @Override
    public int deleteSearchHistoryByIds(Long[] ids)
    {
        return searchHistoryMapper.deleteSearchHistoryByIds(ids);
    }

    /**
     * 删除搜索历史信息
     * 
     * @param id 搜索历史主键
     * @return 结果
     */
    @Override
    public int deleteSearchHistoryById(Long id)
    {
        return searchHistoryMapper.deleteSearchHistoryById(id);
    }
}
