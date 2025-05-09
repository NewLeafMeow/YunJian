package com.yunjian.profile.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.profile.mapper.UserProfileMapper;
import com.yunjian.profile.domain.UserProfile;
import com.yunjian.profile.service.IUserProfileService;

/**
 * 用户画像Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-05-02
 */
@Service
public class UserProfileServiceImpl implements IUserProfileService 
{
    @Autowired
    private UserProfileMapper userProfileMapper;

    /**
     * 查询用户画像
     * 
     * @param id 用户画像主键
     * @return 用户画像
     */
    @Override
    public UserProfile selectUserProfileById(Long id)
    {
        return userProfileMapper.selectUserProfileById(id);
    }

    /**
     * 查询用户画像列表
     * 
     * @param userProfile 用户画像
     * @return 用户画像
     */
    @Override
    public List<UserProfile> selectUserProfileList(UserProfile userProfile)
    {
        return userProfileMapper.selectUserProfileList(userProfile);
    }

    /**
     * 新增用户画像
     * 
     * @param userProfile 用户画像
     * @return 结果
     */
    @Override
    public int insertUserProfile(UserProfile userProfile)
    {
        return userProfileMapper.insertUserProfile(userProfile);
    }

    /**
     * 修改用户画像
     * 
     * @param userProfile 用户画像
     * @return 结果
     */
    @Override
    public int updateUserProfile(UserProfile userProfile)
    {
        return userProfileMapper.updateUserProfile(userProfile);
    }

    /**
     * 批量删除用户画像
     * 
     * @param ids 需要删除的用户画像主键
     * @return 结果
     */
    @Override
    public int deleteUserProfileByIds(Long[] ids)
    {
        return userProfileMapper.deleteUserProfileByIds(ids);
    }

    /**
     * 删除用户画像信息
     * 
     * @param id 用户画像主键
     * @return 结果
     */
    @Override
    public int deleteUserProfileById(Long id)
    {
        return userProfileMapper.deleteUserProfileById(id);
    }
}
