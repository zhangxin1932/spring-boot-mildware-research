package com.zy.spring.mildware.test.spring.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ZtreeDTO {

    private String id;

    private String pId;

    private String name;

    private Boolean open;

    private Boolean isParent;
}
