package com.zy.spring.mildware.rdbms.jpa.dsl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 员工表
 * javax.persistence 的注解, 仅在 jpa 中被使用, mybatis 不影响
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_employee")
public class TbEmployee implements Serializable {
    private static final long serialVersionUID = 8435846164595448052L;
    /**
     * 员工 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 员工姓名
     */
    @Column(name = "employee_name")
    private String employeeName;
    /**
     * 员工性别
     */
    @Column(name = "gender")
    private String gender;
    /**
     * 出生年月
     * spring 的 @DateTimeFormat 注解用于入参格式化
     * jackson 的 @JsonFormat 注解用于出参格式化
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    // @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birthday")
    private Date birthday;
    /**
     * 员工部门 id
     */
    @Column(name = "department_id")
    private Long departmentId;
    /**
     * 员工部门名称
     */
    @Column(name = "department_name")
    private String departmentName;
    /**
     * 员工职位 id
     */
    @Column(name = "job_id")
    private Long jobId;
    /**
     * 员工职位名称
     */
    @Column(name = "job_name")
    private String jobName;
    /**
     * 员工直属领导 id
     */
    @Column(name = "leader_id")
    private Long leaderId;
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
