package com.zy.spring.mildware.rdbms.jpa.dsl.qentity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.zy.spring.mildware.rdbms.jpa.dsl.entity.TbDepartment;


/**
 * QTbDepartment is a Querydsl query type for TbDepartment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QTbDepartment extends EntityPathBase<TbDepartment> {

    private static final long serialVersionUID = 104018058L;

    public static final QTbDepartment tbDepartment = new QTbDepartment("tbDepartment");

    public final StringPath departmentName = createString("departmentName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.util.Date> updateTime = createDateTime("updateTime", java.util.Date.class);

    public final NumberPath<Long> updateUser = createNumber("updateUser", Long.class);

    public QTbDepartment(String variable) {
        super(TbDepartment.class, forVariable(variable));
    }

    public QTbDepartment(Path<? extends TbDepartment> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTbDepartment(PathMetadata metadata) {
        super(TbDepartment.class, metadata);
    }

}

