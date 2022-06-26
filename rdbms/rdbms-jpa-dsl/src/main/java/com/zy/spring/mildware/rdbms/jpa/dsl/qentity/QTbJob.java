package com.zy.spring.mildware.rdbms.jpa.dsl.qentity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.zy.spring.mildware.rdbms.jpa.dsl.entity.TbJob;


/**
 * QTbJob is a Querydsl query type for TbJob
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTbJob extends EntityPathBase<TbJob> {

    private static final long serialVersionUID = 1242865029L;

    public static final QTbJob tbJob = new QTbJob("tbJob");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath jobName = createString("jobName");

    public final DateTimePath<java.util.Date> updateTime = createDateTime("updateTime", java.util.Date.class);

    public final NumberPath<Long> updateUser = createNumber("updateUser", Long.class);

    public QTbJob(String variable) {
        super(TbJob.class, forVariable(variable));
    }

    public QTbJob(Path<? extends TbJob> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbJob(PathMetadata metadata) {
        super(TbJob.class, metadata);
    }

}

