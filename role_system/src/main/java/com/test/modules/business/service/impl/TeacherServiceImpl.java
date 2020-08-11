package com.test.modules.business.service.impl;

import com.test.modules.business.entity.Teacher;
import com.test.modules.business.mapper.TeacherMapper;
import com.test.modules.business.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
* <p>
* 教师 服务实现类
* </p>
*
* @author RenShiWei
* @since 2020-08-06
*/
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

    private final TeacherMapper teacherMapper;

    public TeacherServiceImpl(TeacherMapper teacherMapper){
        this.teacherMapper= teacherMapper;
    }

}

