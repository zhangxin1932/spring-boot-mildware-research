package com.zy.spring.mildware.test.spring.boot.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FileUrlMapper {

    List<String> getFatherFileUrls();

    List<String> getSonFileUrls(@Param("generationNum") Integer generationNum,
                                @Param("filePath") String filePath);
}
