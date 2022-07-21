package com.zy.spring.mildware.test.spring.boot.config;


import com.zy.spring.mildware.test.spring.boot.entity.ZtreeDTO;
import com.zy.spring.mildware.test.spring.boot.service.IFileUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 项目启动时加载数据的类
 */
@Component
public class InitData implements CommandLineRunner {

    @Autowired
    private IFileUrlService fileUrlService;

    @Override
    public void run(String... strings) throws Exception {
        getFatherFileUrls();
    }

    @CachePut(value = "fileUrls")
    public List<ZtreeDTO> getFatherFileUrls(){
        return fileUrlService.getFatherFileUrls();
    }
}
