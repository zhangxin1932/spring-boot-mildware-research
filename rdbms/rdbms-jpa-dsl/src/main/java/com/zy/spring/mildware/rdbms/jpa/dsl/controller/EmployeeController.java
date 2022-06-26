package com.zy.spring.mildware.rdbms.jpa.dsl.controller;

import com.google.common.collect.Lists;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zy.spring.mildware.rdbms.jpa.dsl.common.Quarter;
import com.zy.spring.mildware.rdbms.jpa.dsl.dto.EmployeeDTO;
import com.zy.spring.mildware.rdbms.jpa.dsl.dto.QueryEmployeePerformancePageListReq;
import com.zy.spring.mildware.rdbms.jpa.dsl.entity.TbEmployee;
import com.zy.spring.mildware.rdbms.jpa.dsl.qentity.QTbEmployee;
import com.zy.spring.mildware.rdbms.jpa.dsl.qentity.QTbPerformance;
import com.zy.spring.mildware.rdbms.jpa.dsl.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * 1.jpa 官网
 * https://docs.spring.io/spring-data/jpa/docs/
 * <p>
 * 2.事务
 * <p>
 * 3.分页
 * <p>
 * 4.复杂 sql 映射
 * <p>
 * 5.queryDSL
 * http://www.querydsl.com/static/querydsl/4.1.3/reference/html_single/
 *
 *
 * 参考
 * https://blog.csdn.net/phapha1996/article/details/83614975
 * https://blog.csdn.net/qq_30054997/article/details/79420141
 * https://blog.csdn.net/qq_34532187/article/details/84594730
 */
@RestController
@RequestMapping("/jpa/employee/")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    /**
     * 单表分页查询
     * @param page
     * @return
     */
    @RequestMapping("queryEmployeePageList")
    public Page<TbEmployee> queryEmployeePageList(Pageable page) {
        return employeeRepository.findAll(page);
    }

    /**
     * QueryDSL
     * 多表联查, 分页查询, 排序查询
     * @param req
     * @param page
     * @return
     */
    @RequestMapping("queryEmployeeConditionPageList")
    public Page<EmployeeDTO> queryEmployeeConditionPageList(@RequestBody(required = false) QueryEmployeePerformancePageListReq req, Pageable page) {
        final StringPath sp;
        if (Objects.nonNull(req)) {
            String quarter = req.getQuarter();
            if (Objects.equals(Quarter.Q.getName(), quarter)) {
                sp = QTbPerformance.tbPerformance.qDegree;
            } else if (Objects.equals(Quarter.Q1.getName(), quarter)) {
                sp = QTbPerformance.tbPerformance.q1Degree;
            } else if (Objects.equals(Quarter.Q2.getName(), quarter)) {
                sp = QTbPerformance.tbPerformance.q2Degree;
            } else if (Objects.equals(Quarter.Q3.getName(), quarter)) {
                sp = QTbPerformance.tbPerformance.q3Degree;
            } else if (Objects.equals(Quarter.Q4.getName(), quarter)) {
                sp = QTbPerformance.tbPerformance.q4Degree;
            } else {
                // 默认走 Q1 的绩效
                sp = QTbPerformance.tbPerformance.q1Degree;
            }
        } else {
            // 默认走 Q1
            sp = QTbPerformance.tbPerformance.q1Degree;
        }
        // 多表动态分页查询
        JPAQuery<Tuple> query = jpaQueryFactory.select(QTbEmployee.tbEmployee.id, QTbEmployee.tbEmployee.employeeName, QTbEmployee.tbEmployee.jobName, QTbEmployee.tbEmployee.departmentName, QTbEmployee.tbEmployee.gender,
                sp)
                .from(QTbEmployee.tbEmployee)
                .leftJoin(QTbPerformance.tbPerformance)
                .on(QTbEmployee.tbEmployee.id.longValue().eq(QTbPerformance.tbPerformance.employeeId.longValue()));
        if (Objects.nonNull(req)) {
            Long jobId = req.getJobId();
            Long departmentId = req.getDepartmentId();
            String employeeName = req.getEmployeeName();
            String year = req.getPerformanceYear();
            if (Objects.nonNull(employeeName) && employeeName.trim().length() != 0) {
                query.where(QTbEmployee.tbEmployee.employeeName.like(employeeName));
            }
            if (Objects.nonNull(jobId)) {
                query.where(QTbEmployee.tbEmployee.jobId.eq(jobId));
            }
            if (Objects.nonNull(departmentId)) {
                query.where(QTbEmployee.tbEmployee.departmentId.eq(departmentId));
            }
            if (Objects.nonNull(year) && year.trim().length() != 0) {
                query.where(QTbPerformance.tbPerformance.performanceYear.eq(year));
            }
        }
        query.orderBy(QTbEmployee.tbEmployee.employeeName.desc()).offset(page.getOffset()).limit(page.getPageSize());
        QueryResults<Tuple> results = query.fetchResults();
        if (Objects.isNull(results) || CollectionUtils.isEmpty(results.getResults())) {
            return null;
        }
        List<EmployeeDTO> contents = Lists.newArrayList();
        results.getResults().forEach(tuple -> {
            if (Objects.isNull(tuple)) {
                return;
            }
            EmployeeDTO build = EmployeeDTO.builder()
                    .id(tuple.get(QTbEmployee.tbEmployee.id))
                    .employeeName(tuple.get(QTbEmployee.tbEmployee.employeeName))
                    .jobName(tuple.get(QTbEmployee.tbEmployee.jobName))
                    .departmentName(tuple.get(QTbEmployee.tbEmployee.departmentName))
                    .gender(tuple.get(QTbEmployee.tbEmployee.gender))
                    .performanceDegree(tuple.get(sp))
                    .build();
            contents.add(build);
        });
        return new PageImpl<>(contents, page, results.getTotal());
    }

}
