package com.zy.spring.mildware.rdbms.jpa.dsl.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class EmployeeDTO implements Serializable {
    private static final long serialVersionUID = -6589018701020361515L;
    private Long id;
    private String employeeName;
    private String gender;
    private String departmentName;
    private String jobName;
    /**
     * 这里查询的是某一年度的总绩效 或者 某一年度某一季度的绩效
     */
    private String performanceDegree;
}
