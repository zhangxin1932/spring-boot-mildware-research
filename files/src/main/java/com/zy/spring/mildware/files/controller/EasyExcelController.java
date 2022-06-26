package com.zy.spring.mildware.files.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.zy.spring.mildware.files.easyexcel.StuScoreExcel;
import com.zy.spring.mildware.files.easyexcel.StuScoreListener;
import com.zy.spring.mildware.files.service.StuServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class EasyExcelController {

    @Autowired
    private StuServiceImpl stuService;

    /**
     * 文件下载（失败了会返回一个有部分数据的Excel）
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link StuScoreExcel}
     * <p>
     * 2. 设置返回的 参数
     * <p>
     * 3. 直接写，这里注意，finish的时候会自动关闭OutputStream,当然你外面再关闭流问题不大
     */
    @GetMapping("download")
    public void download(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用 swagger 会导致各种问题，请直接用浏览器或者用 postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("学生成绩表", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), StuScoreExcel.class).sheet("第一次月考成绩").doWrite(data());
    }

    /**
     * 文件下载并且失败的时候返回json（默认失败了会返回一个有部分数据的Excel）
     *
     * @since 2.1.1
     */
    @GetMapping("downloadFailedUsingJson")
    public void downloadFailedUsingJson(HttpServletResponse response) throws IOException {
        // 这里注意 有同学反应使用 swagger 会导致各种问题，请直接用浏览器或者用 postman
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            // 这里 URLEncoder.encode 可以防止中文乱码 当然和 easyexcel 没有关系
            String fileName = URLEncoder.encode("测试", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), StuScoreExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板").doWrite(data());
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = new HashMap<>();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JSON.toJSONString(map));
        }
    }

    /**
     * 文件上传
     * <p>
     * 1. 创建excel对应的实体对象 参照{@link StuScoreExcel}
     * <p>
     * 2. 由于默认异步读取excel，所以需要创建 excel 一行一行的回调监听器，参照{@link StuScoreListener}
     * <p>
     * 3. 直接读即可
     */
    @PostMapping("uploadStuScoreFile")
    public String upload(@RequestParam("uploadStuScoreFile") MultipartFile file) throws Exception {
        EasyExcel.read(file.getInputStream(), StuScoreExcel.class, new StuScoreListener(stuService)).sheet().doRead();
        return "success";
    }

    private List<StuScoreExcel> data() {
        try {
            List<StuScoreExcel> list = stuService.getAllScore();
            if (CollectionUtils.isNotEmpty(list)) {
                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
