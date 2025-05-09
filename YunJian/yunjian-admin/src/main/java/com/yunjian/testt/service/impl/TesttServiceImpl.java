package com.yunjian.testt.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yunjian.testt.mapper.TesttMapper;
import com.yunjian.testt.domain.Testt;
import com.yunjian.testt.service.ITesttService;

/**
 * 测试Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-04-12
 */
@Service
public class TesttServiceImpl implements ITesttService 
{
    @Autowired
    private TesttMapper testtMapper;

    /**
     * 查询测试
     * 
     * @param id 测试主键
     * @return 测试
     */
    @Override
    public Testt selectTesttById(Long id)
    {
        return testtMapper.selectTesttById(id);
    }

    /**
     * 查询测试列表
     * 
     * @param testt 测试
     * @return 测试
     */
    @Override
    public List<Testt> selectTesttList(Testt testt)
    {
        return testtMapper.selectTesttList(testt);
    }

    /**
     * 新增测试
     * 
     * @param testt 测试
     * @return 结果
     */
    @Override
    public int insertTestt(Testt testt)
    {
        return testtMapper.insertTestt(testt);
    }

    /**
     * 修改测试
     * 
     * @param testt 测试
     * @return 结果
     */
    @Override
    public int updateTestt(Testt testt)
    {
        return testtMapper.updateTestt(testt);
    }

    /**
     * 批量删除测试
     * 
     * @param ids 需要删除的测试主键
     * @return 结果
     */
    @Override
    public int deleteTesttByIds(Long[] ids)
    {
        return testtMapper.deleteTesttByIds(ids);
    }

    /**
     * 删除测试信息
     * 
     * @param id 测试主键
     * @return 结果
     */
    @Override
    public int deleteTesttById(Long id)
    {
        return testtMapper.deleteTesttById(id);
    }
}
