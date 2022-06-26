package com.zy.spring.mildware.rdbms.multi.datasource.mapper.master;


import com.zy.spring.mildware.rdbms.multi.datasource.bean.Stu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StuMasterMapper {

    List<Stu> getAllStu();
}
