package com.zy.spring.mildware.rdbms.multi.datasource.contoller;

import com.alibaba.fastjson.JSON;
import com.zy.spring.mildware.rdbms.multi.datasource.bean.Stu;
import com.zy.spring.mildware.rdbms.multi.datasource.service.StuServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/stu/", produces = MediaType.APPLICATION_JSON_VALUE)
public class StuController {

    @Autowired
    private StuServiceImpl stuService;

    @PostMapping("/map")
    public String map(@RequestBody String params){
        Map<String, String> map = JSON.parseObject(params, Map.class);
        Set<String> keys = map.keySet();
        for (String key : keys){
            System.out.println(key + "=============" + map.get(key));
        }
        return params;
    }

    @PostMapping("twoDataSource")
    public List<Stu> twoDataSource(){
        return stuService.hello();
    }

    @GetMapping("getAllStu")
    public List<Stu> getAllStu(){
        return stuService.hello();
    }

}
