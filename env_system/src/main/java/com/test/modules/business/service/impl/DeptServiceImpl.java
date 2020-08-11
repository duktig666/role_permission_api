package com.test.modules.business.service.impl;

import com.test.modules.business.entity.Dept;
import com.test.modules.business.mapper.DeptMapper;
import com.test.modules.business.service.IDeptService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Service
@Primary
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

}
