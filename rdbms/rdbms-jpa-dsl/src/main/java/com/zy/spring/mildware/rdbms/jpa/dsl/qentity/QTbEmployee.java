package com.zy.spring.mildware.rdbms.jpa.dsl.qentity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.zy.spring.mildware.rdbms.jpa.dsl.entity.TbEmployee;


/**
 * QTbEmployee is a Querydsl query type for TbEmployee
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTbEmployee extends EntityPathBase<TbEmployee> {

    private static final long serialVersionUID = 1527890406L;

    public static final QTbEmployee tbEmployee = new QTbEmployee("tbEmployee");

    public final DateTimePath<java.util.Date> birthday = createDateTime("birthday", java.util.Date.class);

    public final NumberPath<Long> departmentId = createNumber("departmentId", Long.class);

    public final StringPath departmentName = createString("departmentName");

    public final StringPath employeeName = createString("employeeName");

    public final StringPath gender = createString("gender");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> jobId = createNumber("jobId", Long.class);

    public final StringPath jobName = createString("jobName");

    public final NumberPath<Long> leaderId = createNumber("leaderId", Long.class);

    public final DateTimePath<java.util.Date> updateTime = createDateTime("updateTime", java.util.Date.class);

    public final NumberPath<Long> updateUser = createNumber("updateUser", Long.class);

    public QTbEmployee(String variable) {
        super(TbEmployee.class, forVariable(variable));
    }

    public QTbEmployee(Path<? extends TbEmployee> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbEmployee(PathMetadata metadata) {
        super(TbEmployee.class, metadata);
    }

}

