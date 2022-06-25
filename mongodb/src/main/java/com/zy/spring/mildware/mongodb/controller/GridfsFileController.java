package com.zy.spring.mildware.mongodb.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.gridfs.GridFsUpload;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * https://blog.csdn.net/ai_0922/article/details/105192399
 * https://blog.csdn.net/daqi1983/article/details/121739420 (文件下载时, 中文乱码方案)
 */
@RestController
@RequestMapping("/file/")
@Slf4j
public class GridfsFileController {

    @Resource(name = "tipsGridFsTemplate")
    private GridFsTemplate tipsGridFsTemplate;

    @PostMapping("upload")
    public Object upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        // CommonsFileUploadSupport
        try {
            String fileName = file.getName();
            String contentType = file.getContentType();
            Assert.hasText(fileName, "filename cannot be empty.");
            Assert.hasText(contentType, "contentType cannot be empty.");
            GridFsUpload.GridFsUploadBuilder<ObjectId> uploadBuilder = GridFsUpload.fromStream(file.getInputStream());
            ObjectId objectId = new ObjectId();
            uploadBuilder.id(objectId);
            Document metaData = new Document();
            metaData.put("timestamp", DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()));
            if (StringUtils.hasText(fileName)) {
                uploadBuilder.filename(fileName);
            }
            if (StringUtils.hasText(contentType)) {
                uploadBuilder.contentType(contentType);
                metaData.put("content-type", contentType);
            }
            uploadBuilder.metadata(metaData);
            return tipsGridFsTemplate.store(uploadBuilder.build()).toHexString();
        } catch (Exception e) {
            log.error("failed to store file:{}.", file.getName(), e);
        }
        return null;
    }

    @RequestMapping("download")
    public void download(@RequestParam("fileId") String fileId, HttpServletResponse response) {
        try {
            GridFSFile file = tipsGridFsTemplate.findOne(Query.query(Criteria.where("_id").is(fileId)));
            if (Objects.isNull(file)) {
                return;
            }
            GridFsResource resource = tipsGridFsTemplate.getResource(file);
            InputStream content = resource.getContent();
            // 设置ContentType字段值
            response.setContentType(file.getMetadata().getString("content-type"));
            // 设置响应消息编码
            response.setCharacterEncoding("utf-8");
            // 通知浏览器以下载的方式打开
            response.addHeader("Content-Disposition",
                    "attachment;filename="+ URLEncoder.encode(resource.getFilename(),"utf-8"));
            // 获取response对象的输出流
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            //循环取出流中的数据
            while ((len = content.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("failed to download file, id is:{}.", fileId, e);
        }
    }

    @RequestMapping("checkFileName")
    public Boolean checkFileName(@RequestParam("filename") String filename) {
        Assert.hasText(filename, "filename cannot be empty.");
        return Objects.isNull(tipsGridFsTemplate.findOne(Query.query(Criteria.where("filename").is(filename))));
    }

    @RequestMapping("deleteById")
    public void deleteById(@RequestParam("fileId") String fileId) {
        Assert.hasText(fileId, "fileId cannot be empty.");
        tipsGridFsTemplate.delete(Query.query(Criteria.where("_id").is(fileId)));
    }

}
