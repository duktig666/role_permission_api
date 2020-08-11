package com.test.modules.business.mapper;

import com.test.modules.business.entity.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.test.modules.business.entity.bo.JobSmallBO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 岗位 Mapper 接口
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Component
public interface JobMapper extends BaseMapper<Job> {

    /**
     * description: 根据岗位id查询
     * @param id 岗位id
     * @return 最小化岗位信息
     *
     * @author RenShiWei
     * Date: 2020/8/7 21:21
     */
    @Select("SELECT id,name FROM job WHERE id=#{id}")
    JobSmallBO findJobSmallBoById(int id);

}
