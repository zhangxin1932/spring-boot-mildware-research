package com.zy.spring.mildware.test.spring.boot.controller;

import com.alibaba.fastjson.JSON;
import com.zy.spring.mildware.test.spring.boot.entity.ZtreeDTO;
import com.zy.spring.mildware.test.spring.boot.service.IFileUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * https://www.cnblogs.com/quintanliu/p/13423292.html
 */
@Controller
@RequestMapping("/files/")
public class FileUrlController {

    @Autowired
    private IFileUrlService fileUrlService;

    @RequestMapping("getFatherFileUrls")
    public String getFatherFileUrls(Map map){
        List<ZtreeDTO> list = fileUrlService.getFatherFileUrls();
        Object json = JSON.toJSON(list);
        map.put("fatherFileUrls", json);
        return "fileUrl";
    }

    @RequestMapping("getSonFileUrls")
    @ResponseBody
    public Object getSonFileUrls(String id){
        List<ZtreeDTO> list = fileUrlService.getSonFileUrls(id);
        Object json = JSON.toJSON(list);
        return json;
    }
}
