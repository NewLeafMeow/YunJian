package com.yunjian.profile.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.yunjian.common.annotation.Excel;
import com.yunjian.common.core.domain.BaseEntity;

/**
 * 用户画像对象 user_profile
 * 
 * @author ruoyi
 * @date 2025-05-02
 */
public class UserProfile extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 用户画像表的唯一标识，自增主键 */
    private Long id;

    /** 关联 sys_user 表的用户 ID，用于标识用户 */
    @Excel(name = "关联 sys_user 表的用户 ID，用于标识用户")
    private Long userId;

    /** 用户点击财经类帖子的次数 */
    @Excel(name = "用户点击财经类帖子的次数")
    private Long financeClickCount;

    /** 用户点赞财经类帖子的次数 */
    @Excel(name = "用户点赞财经类帖子的次数")
    private Long financeLikeCount;

    /** 用户点击时政类帖子的次数 */
    @Excel(name = "用户点击时政类帖子的次数")
    private Long politicsClickCount;

    /** 用户点赞时政类帖子的次数 */
    @Excel(name = "用户点赞时政类帖子的次数")
    private Long politicsLikeCount;

    /** 用户点击房产类帖子的次数 */
    @Excel(name = "用户点击房产类帖子的次数")
    private Long realEstateClickCount;

    /** 用户点赞房产类帖子的次数 */
    @Excel(name = "用户点赞房产类帖子的次数")
    private Long realEstateLikeCount;

    /** 用户点击家居类帖子的次数 */
    @Excel(name = "用户点击家居类帖子的次数")
    private Long homeClickCount;

    /** 用户点赞家居类帖子的次数 */
    @Excel(name = "用户点赞家居类帖子的次数")
    private Long homeLikeCount;

    /** 用户点击科技类帖子的次数 */
    @Excel(name = "用户点击科技类帖子的次数")
    private Long technologyClickCount;

    /** 用户点赞科技类帖子的次数 */
    @Excel(name = "用户点赞科技类帖子的次数")
    private Long technologyLikeCount;

    /** 用户点击体育类帖子的次数 */
    @Excel(name = "用户点击体育类帖子的次数")
    private Long sportsClickCount;

    /** 用户点赞体育类帖子的次数 */
    @Excel(name = "用户点赞体育类帖子的次数")
    private Long sportsLikeCount;

    /** 用户点击游戏类帖子的次数 */
    @Excel(name = "用户点击游戏类帖子的次数")
    private Long gamingClickCount;

    /** 用户点赞游戏类帖子的次数 */
    @Excel(name = "用户点赞游戏类帖子的次数")
    private Long gamingLikeCount;

    /** 用户点击时尚类帖子的次数 */
    @Excel(name = "用户点击时尚类帖子的次数")
    private Long fashionClickCount;

    /** 用户点赞时尚类帖子的次数 */
    @Excel(name = "用户点赞时尚类帖子的次数")
    private Long fashionLikeCount;

    /** 用户点击教育类帖子的次数 */
    @Excel(name = "用户点击教育类帖子的次数")
    private Long educationClickCount;

    /** 用户点赞教育类帖子的次数 */
    @Excel(name = "用户点赞教育类帖子的次数")
    private Long educationLikeCount;

    /** 用户点击娱乐类帖子的次数 */
    @Excel(name = "用户点击娱乐类帖子的次数")
    private Long entertainmentClickCount;

    /** 用户点赞娱乐类帖子的次数 */
    @Excel(name = "用户点赞娱乐类帖子的次数")
    private Long entertainmentLikeCount;

    /** 用户的搜索记录类型 */
    @Excel(name = "用户的搜索记录类型")
    private String searchRecordType;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setFinanceClickCount(Long financeClickCount) 
    {
        this.financeClickCount = financeClickCount;
    }

    public Long getFinanceClickCount() 
    {
        return financeClickCount;
    }
    public void setFinanceLikeCount(Long financeLikeCount) 
    {
        this.financeLikeCount = financeLikeCount;
    }

    public Long getFinanceLikeCount() 
    {
        return financeLikeCount;
    }
    public void setPoliticsClickCount(Long politicsClickCount) 
    {
        this.politicsClickCount = politicsClickCount;
    }

    public Long getPoliticsClickCount() 
    {
        return politicsClickCount;
    }
    public void setPoliticsLikeCount(Long politicsLikeCount) 
    {
        this.politicsLikeCount = politicsLikeCount;
    }

    public Long getPoliticsLikeCount() 
    {
        return politicsLikeCount;
    }
    public void setRealEstateClickCount(Long realEstateClickCount) 
    {
        this.realEstateClickCount = realEstateClickCount;
    }

    public Long getRealEstateClickCount() 
    {
        return realEstateClickCount;
    }
    public void setRealEstateLikeCount(Long realEstateLikeCount) 
    {
        this.realEstateLikeCount = realEstateLikeCount;
    }

    public Long getRealEstateLikeCount() 
    {
        return realEstateLikeCount;
    }
    public void setHomeClickCount(Long homeClickCount) 
    {
        this.homeClickCount = homeClickCount;
    }

    public Long getHomeClickCount() 
    {
        return homeClickCount;
    }
    public void setHomeLikeCount(Long homeLikeCount) 
    {
        this.homeLikeCount = homeLikeCount;
    }

    public Long getHomeLikeCount() 
    {
        return homeLikeCount;
    }
    public void setTechnologyClickCount(Long technologyClickCount) 
    {
        this.technologyClickCount = technologyClickCount;
    }

    public Long getTechnologyClickCount() 
    {
        return technologyClickCount;
    }
    public void setTechnologyLikeCount(Long technologyLikeCount) 
    {
        this.technologyLikeCount = technologyLikeCount;
    }

    public Long getTechnologyLikeCount() 
    {
        return technologyLikeCount;
    }
    public void setSportsClickCount(Long sportsClickCount) 
    {
        this.sportsClickCount = sportsClickCount;
    }

    public Long getSportsClickCount() 
    {
        return sportsClickCount;
    }
    public void setSportsLikeCount(Long sportsLikeCount) 
    {
        this.sportsLikeCount = sportsLikeCount;
    }

    public Long getSportsLikeCount() 
    {
        return sportsLikeCount;
    }
    public void setGamingClickCount(Long gamingClickCount) 
    {
        this.gamingClickCount = gamingClickCount;
    }

    public Long getGamingClickCount() 
    {
        return gamingClickCount;
    }
    public void setGamingLikeCount(Long gamingLikeCount) 
    {
        this.gamingLikeCount = gamingLikeCount;
    }

    public Long getGamingLikeCount() 
    {
        return gamingLikeCount;
    }
    public void setFashionClickCount(Long fashionClickCount) 
    {
        this.fashionClickCount = fashionClickCount;
    }

    public Long getFashionClickCount() 
    {
        return fashionClickCount;
    }
    public void setFashionLikeCount(Long fashionLikeCount) 
    {
        this.fashionLikeCount = fashionLikeCount;
    }

    public Long getFashionLikeCount() 
    {
        return fashionLikeCount;
    }
    public void setEducationClickCount(Long educationClickCount) 
    {
        this.educationClickCount = educationClickCount;
    }

    public Long getEducationClickCount() 
    {
        return educationClickCount;
    }
    public void setEducationLikeCount(Long educationLikeCount) 
    {
        this.educationLikeCount = educationLikeCount;
    }

    public Long getEducationLikeCount() 
    {
        return educationLikeCount;
    }
    public void setEntertainmentClickCount(Long entertainmentClickCount) 
    {
        this.entertainmentClickCount = entertainmentClickCount;
    }

    public Long getEntertainmentClickCount() 
    {
        return entertainmentClickCount;
    }
    public void setEntertainmentLikeCount(Long entertainmentLikeCount) 
    {
        this.entertainmentLikeCount = entertainmentLikeCount;
    }

    public Long getEntertainmentLikeCount() 
    {
        return entertainmentLikeCount;
    }
    public void setSearchRecordType(String searchRecordType) 
    {
        this.searchRecordType = searchRecordType;
    }

    public String getSearchRecordType() 
    {
        return searchRecordType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("financeClickCount", getFinanceClickCount())
            .append("financeLikeCount", getFinanceLikeCount())
            .append("politicsClickCount", getPoliticsClickCount())
            .append("politicsLikeCount", getPoliticsLikeCount())
            .append("realEstateClickCount", getRealEstateClickCount())
            .append("realEstateLikeCount", getRealEstateLikeCount())
            .append("homeClickCount", getHomeClickCount())
            .append("homeLikeCount", getHomeLikeCount())
            .append("technologyClickCount", getTechnologyClickCount())
            .append("technologyLikeCount", getTechnologyLikeCount())
            .append("sportsClickCount", getSportsClickCount())
            .append("sportsLikeCount", getSportsLikeCount())
            .append("gamingClickCount", getGamingClickCount())
            .append("gamingLikeCount", getGamingLikeCount())
            .append("fashionClickCount", getFashionClickCount())
            .append("fashionLikeCount", getFashionLikeCount())
            .append("educationClickCount", getEducationClickCount())
            .append("educationLikeCount", getEducationLikeCount())
            .append("entertainmentClickCount", getEntertainmentClickCount())
            .append("entertainmentLikeCount", getEntertainmentLikeCount())
            .append("searchRecordType", getSearchRecordType())
            .toString();
    }
}
