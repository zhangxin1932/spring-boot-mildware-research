package com.zy.spring.mildware.misc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class FreemarkerController {

    /**
     * ps：这里dataModel的map只能从方法参数中获取，如果是自己new的去存储数据，填充到模板中，会报空值异常。
     * 部署完后, 访问 http://localhost:8080/f1 即可
     *
     * @param dataModel
     * @return
     */
    @RequestMapping("/f1")
    public String f1(Map<String, Object> dataModel) {
        dataModel.put("name", "张三");
        dataModel.put("message", "hello world");

        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> map1 = new HashMap<>();
        map1.put("name", "苹果");
        map1.put("price", 4.5);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name", "香蕉");
        map2.put("price", 6.3);
        list.add(map1);
        list.add(map2);
        dataModel.put("goodsList", list);
        dataModel.put("today", new Date());
        dataModel.put("number", 123456789L);
        return "index";
    }

    @RequestMapping("get")
    @ResponseBody
    public Object get(HttpServletRequest request){
        Object username = request.getSession().getAttribute("username");
        return username;
    }

    /**
     * https://blog.csdn.net/menghuanzhiming/article/details/102736312
     * @param request
     * @return
     */
    @RequestMapping("set")
    @ResponseBody
    public String set(HttpServletRequest request){
        request.getSession().setAttribute("username", "tommy");
        return request.getSession().getId();
    }
}
