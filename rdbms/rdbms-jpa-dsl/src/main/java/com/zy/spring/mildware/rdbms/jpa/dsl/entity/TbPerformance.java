package com.zy.spring.mildware.rdbms.jpa.dsl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 绩效表
 * javax.persistence 的注解, 仅在 jpa 中被使用, mybatis 不影响
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_performance")
public class TbPerformance implements Serializable {
    private static final long serialVersionUID = -7126363726476136274L;
    /**
     * 绩效 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 年度: 格式为 yyyy
     */
    @Column(name = "performance_year")
    private String performanceYear;
    /**
     * 员工 id
     */
    @Column(name = "employee_id")
    private Long employeeId;
    /**
     * 年度绩效 {@link com.zy.common.enums.PerformanceDegree}
     */
    @Column(name = "q_degree")
    private String qDegree;
    /**
     * Q1 绩效
     * {@link com.zy.common.enums.Quarter}
     */
    @Column(name = "q1_degree")
    private String q1Degree;
    /**
     * Q2 绩效
     * {@link com.zy.common.enums.Quarter}
     */
    @Column(name = "q2_degree")
    private String q2Degree;
    /**
     * Q3 绩效
     * {@link com.zy.common.enums.Quarter}
     */
    @Column(name = "q3_degree")
    private String q3Degree;
    /**
     * Q4 绩效
     * {@link com.zy.common.enums.Quarter}
     */
    @Column(name = "q4_degree")
    private String q4Degree;
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
