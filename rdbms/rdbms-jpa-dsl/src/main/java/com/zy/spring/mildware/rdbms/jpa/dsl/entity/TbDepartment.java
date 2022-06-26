package com.zy.spring.mildware.rdbms.jpa.dsl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 部门表
 * javax.persistence 的注解, 仅在 jpa 中被使用, mybatis 不影响
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_department")
public class TbDepartment implements Serializable {
    private static final long serialVersionUID = -1382834167823127693L;
    /**
     * 员工部门 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 员工部门名称
     */
    @Column(name = "department_name")
    private String departmentName;
    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
    /**
     * 更新人
     */
    @Column(name = "update_user")
    private Long updateUser;
}
