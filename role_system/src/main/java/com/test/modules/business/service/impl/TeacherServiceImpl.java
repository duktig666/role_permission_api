package com.test.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * 教师 服务实现类
 *
 * @author RenShiWei
 * @since 2020-08-06
 */
@Service
@Primary
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
        implements IService<Teacher> {

    private final TeacherMapper teacherMapper;

    public TeacherServiceImpl ( TeacherMapper teacherMapper ) {
        this.teacherMapper = teacherMapper;
    }
}
