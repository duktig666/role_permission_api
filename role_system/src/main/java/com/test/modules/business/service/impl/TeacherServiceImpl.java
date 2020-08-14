package com.test.modules.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.test.modules.business.entity.Teacher;
import com.test.modules.business.mapper.TeacherMapper;
import com.test.modules.business.service.ITeacherService;
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
    implements ITeacherService {

  private final TeacherMapper teacherMapper;

  public TeacherServiceImpl(TeacherMapper teacherMapper) {
    this.teacherMapper = teacherMapper;
  }
}
