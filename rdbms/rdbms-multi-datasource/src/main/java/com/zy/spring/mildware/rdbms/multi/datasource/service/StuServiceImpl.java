package com.zy.spring.mildware.rdbms.multi.datasource.service;

import com.zy.spring.mildware.rdbms.multi.datasource.bean.Stu;
import com.zy.spring.mildware.rdbms.multi.datasource.mapper.master.StuMasterMapper;
import com.zy.spring.mildware.rdbms.multi.datasource.mapper.slave.StuSlaveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 事务的传播行为,此处放于类上,也可以放到方法上
 */
@Service("stuService")
public class StuServiceImpl {

    @Autowired
    private StuMasterMapper stuMasterMapper;

    @Autowired
    private StuSlaveMapper stuSlaveMapper;

    public List<Stu> hello(){
        List<Stu> stu = stuMasterMapper.getAllStu();
        List<Stu> stu1 = stuSlaveMapper.getAllStu();
        stu.addAll(stu1);
        return stu;
    }

    @Transactional(transactionManager = "masterTransactionManager", propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertMaster() {

    }

    @Transactional(transactionManager = "slaveTransactionManager", propagation= Propagation.REQUIRED, rollbackFor = Exception.class)
    public void insertSlave() {

    }

}
