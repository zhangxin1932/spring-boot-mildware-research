package com.zy.spring.mildware.rdbms.jpa.dsl.qentity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.zy.spring.mildware.rdbms.jpa.dsl.entity.TbPerformance;


/**
 * QTbPerformance is a Querydsl query type for TbPerformance
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTbPerformance extends EntityPathBase<TbPerformance> {

    private static final long serialVersionUID = 1220266488L;

    public static final QTbPerformance tbPerformance = new QTbPerformance("tbPerformance");

    public final NumberPath<Long> employeeId = createNumber("employeeId", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath performanceYear = createString("performanceYear");

    public final StringPath q1Degree = createString("q1Degree");

    public final StringPath q2Degree = createString("q2Degree");

    public final StringPath q3Degree = createString("q3Degree");

    public final StringPath q4Degree = createString("q4Degree");

    public final StringPath qDegree = createString("qDegree");

    public final DateTimePath<java.util.Date> updateTime = createDateTime("updateTime", java.util.Date.class);

    public final NumberPath<Long> updateUser = createNumber("updateUser", Long.class);

    public QTbPerformance(String variable) {
        super(TbPerformance.class, forVariable(variable));
    }

    public QTbPerformance(Path<? extends TbPerformance> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbPerformance(PathMetadata metadata) {
        super(TbPerformance.class, metadata);
    }

}

