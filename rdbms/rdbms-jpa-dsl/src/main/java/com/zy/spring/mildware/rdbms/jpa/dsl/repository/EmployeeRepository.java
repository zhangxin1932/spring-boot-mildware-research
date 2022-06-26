package com.zy.spring.mildware.rdbms.jpa.dsl.repository;

import com.zy.spring.mildware.rdbms.jpa.dsl.entity.TbEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<TbEmployee, Long> {
}
