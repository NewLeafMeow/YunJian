package com.yunjian.history.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 搜索历史对象 search_history
 * 
 * @author ruoyi
 * @date 2025-05-02
 */
public class SearchHistory extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 搜索记录ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 搜索关键词 */
    @Excel(name = "搜索关键词")
    private String searchTerm;

    /** 搜索时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "搜索时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date searchTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTime(Date searchTime) {
        this.searchTime = searchTime;
    }

    public Date getSearchTime() {
        return searchTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("userId", getUserId())
                .append("searchTerm", getSearchTerm())
                .append("searchTime", getSearchTime())
                .toString();
    }
}
