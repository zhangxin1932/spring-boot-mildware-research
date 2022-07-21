package com.zy.spring.mildware.test.spring.boot.service;

import com.zy.spring.mildware.test.spring.boot.entity.ZtreeDTO;

import java.util.List;

public interface IFileUrlService {

    List<ZtreeDTO> getFatherFileUrls();

    List<ZtreeDTO> getSonFileUrls(String filePath);

}
