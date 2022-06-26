package com.zy.spring.mildware.rdbms.multi.datasource.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stu {

    private Integer id;

    private String name;
}
