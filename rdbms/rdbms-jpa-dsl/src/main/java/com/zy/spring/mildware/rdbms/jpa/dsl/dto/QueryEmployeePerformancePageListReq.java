package com.zy.spring.mildware.rdbms.jpa.dsl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryEmployeePerformancePageListReq implements Serializable {
    private static final long serialVersionUID = 4464478453480330414L;
    private String employeeName;
    private Long departmentId;
    private Long jobId;
    /**
     * 年度
     */
    private String performanceYear;
    /**
     * 季度
     */
    private String quarter;
}
