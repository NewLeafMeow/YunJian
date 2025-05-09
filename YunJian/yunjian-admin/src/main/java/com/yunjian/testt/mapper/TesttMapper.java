package com.yunjian.testt.mapper;

import java.util.List;
import com.yunjian.testt.domain.Testt;

/**
 * 测试Mapper接口
 * 
 * @author ruoyi
 * @date 2025-04-12
 */
public interface TesttMapper 
{
    /**
     * 查询测试
     * 
     * @param id 测试主键
     * @return 测试
     */
    public Testt selectTesttById(Long id);

    /**
     * 查询测试列表
     * 
     * @param testt 测试
     * @return 测试集合
     */
    public List<Testt> selectTesttList(Testt testt);

    /**
     * 新增测试
     * 
     * @param testt 测试
     * @return 结果
     */
    public int insertTestt(Testt testt);

    /**
     * 修改测试
     * 
     * @param testt 测试
     * @return 结果
     */
    public int updateTestt(Testt testt);

    /**
     * 删除测试
     * 
     * @param id 测试主键
     * @return 结果
     */
    public int deleteTesttById(Long id);

    /**
     * 批量删除测试
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTesttByIds(Long[] ids);
}
