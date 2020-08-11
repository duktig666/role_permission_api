package com.test.modules.business.service.impl;

import com.test.modules.business.entity.Job;
import com.test.modules.business.mapper.JobMapper;
import com.test.modules.business.service.IJobService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 岗位 服务实现类
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Service
@Primary
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {

}
