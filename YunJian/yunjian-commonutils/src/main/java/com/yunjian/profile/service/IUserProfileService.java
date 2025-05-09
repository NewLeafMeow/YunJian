package com.yunjian.profile.service;

import java.util.List;
import com.yunjian.profile.domain.UserProfile;

/**
 * 用户画像Service接口
 * 
 * @author ruoyi
 * @date 2025-05-02
 */
public interface IUserProfileService 
{
    /**
     * 查询用户画像
     * 
     * @param id 用户画像主键
     * @return 用户画像
     */
    public UserProfile selectUserProfileById(Long id);

    /**
     * 查询用户画像列表
     * 
     * @param userProfile 用户画像
     * @return 用户画像集合
     */
    public List<UserProfile> selectUserProfileList(UserProfile userProfile);

    /**
     * 新增用户画像
     * 
     * @param userProfile 用户画像
     * @return 结果
     */
    public int insertUserProfile(UserProfile userProfile);

    /**
     * 修改用户画像
     * 
     * @param userProfile 用户画像
     * @return 结果
     */
    public int updateUserProfile(UserProfile userProfile);

    /**
     * 批量删除用户画像
     * 
     * @param ids 需要删除的用户画像主键集合
     * @return 结果
     */
    public int deleteUserProfileByIds(Long[] ids);

    /**
     * 删除用户画像信息
     * 
     * @param id 用户画像主键
     * @return 结果
     */
    public int deleteUserProfileById(Long id);
}
