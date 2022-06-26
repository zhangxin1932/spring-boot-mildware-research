package com.zy.spring.mildware.rdbms.multi.datasource.mapper.slave;

import com.zy.spring.mildware.rdbms.multi.datasource.bean.Stu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StuSlaveMapper {

    List<Stu> getAllStu();
}
