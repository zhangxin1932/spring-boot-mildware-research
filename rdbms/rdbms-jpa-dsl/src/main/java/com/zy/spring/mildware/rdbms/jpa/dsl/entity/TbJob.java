package com.zy.spring.mildware.rdbms.jpa.dsl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 职位表
 * javax.persistence 的注解, 仅在 jpa 中被使用, mybatis 不影响
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_job")
public class TbJob implements Serializable {
    private static final long serialVersionUID = -6069558456473326243L;
    /**
     * 员工职位 id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 员工职位名称
     */
    @Column(name = "job_name")
    private String jobName;
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
