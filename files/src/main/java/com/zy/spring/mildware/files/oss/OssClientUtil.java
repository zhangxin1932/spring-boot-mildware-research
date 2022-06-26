package com.zy.spring.mildware.files.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

/**
 * https://www.cnblogs.com/codigup/p/10598854.html
 * https://blog.csdn.net/weixin_42851487/article/details/84250056
 */
public class OssClientUtil {
    private static Logger logger = LoggerFactory.getLogger(OssClientUtil.class);
    private static String CONTENT_TYPE_IMAGE = "image/jpeg";
    private static OSSClient ossClient;
    private static OSSClient ossClientUrl;
    private static String bucketName = "sunshinetest";
    static {
        String endPoint = "http://oss-cn-beijing.aliyuncs.com/";
        String accessKeyId = "LTAIK9ll0KNxPuDa";
        String accessSecret = "ALESaVe9ldDffrjHS1e3eXtlu0owFg";
        ossClient = new OSSClient(endPoint, accessKeyId, accessSecret);
        ossClientUrl = new OSSClient(endPoint, accessKeyId, accessSecret);
    }

    /**
     *  上传分片文件至阿里云
     * @param file
     * @param key
     * @return
     * @throws IOException
     */
    public static String uploadImg2OSS(MultipartFile file, String key){
        String urlString = null;
        try {
            urlString = uploadFile2OSS(key,file.getInputStream(), CONTENT_TYPE_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlString;
    }

    /*****
     * 将文件上传到阿里云
     * @param key：阿里文件名
     * @param in：文件输入流
     * @return  阿里云访问URL
     */
    private static String uploadFile2OSS(String key, InputStream in, String contentType){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        try {
            objectMetadata.setContentLength(in.available());
            objectMetadata.setCacheControl("no-cache");
            objectMetadata.setHeader("Pragma", "no-cache");
            objectMetadata.setContentType(contentType);

            ossClient.putObject(bucketName, key, in, objectMetadata);

            String urlString = getAccessURL(key);

            logger.info("method[uploadFile2OSS] return url is: " + urlString + ", and key is: " + key);
            return urlString;
        } catch (Exception e) {
            logger.error("alioss put object failed:", e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /****
     *
     * @param key
     * @return
     */
    public static String getAccessURL(String key){
        Date expiration = new Date(new Date().getTime() + 3600 * 1000 * 24 * 365 * 100);    //100年
        URL url = ossClientUrl.generatePresignedUrl(bucketName, key, expiration);
        String urlString = url.toString();
        urlString = urlString.substring(0,urlString.indexOf("?"));//截取？之前的内容，减少空间占用
        logger.info("url: "+urlString);
        return urlString;
    }

    /**
     * Description: 判断OSS服务文件上传时文件的contentType
     *
     * @param FilenameExtension 文件后缀
     * @return String
     */
    public static String getContentType(String FilenameExtension) {
        if (FilenameExtension.equalsIgnoreCase("bmp")) {
            return "image/bmp";
        }
        if (FilenameExtension.equalsIgnoreCase("gif")) {
            return "image/gif";
        }
        if (FilenameExtension.equalsIgnoreCase("jpeg") ||
                FilenameExtension.equalsIgnoreCase("jpg") ||
                FilenameExtension.equalsIgnoreCase("png")) {
            return "image/jpeg";
        }
        if (FilenameExtension.equalsIgnoreCase("html")) {
            return "text/html";
        }
        if (FilenameExtension.equalsIgnoreCase("txt")) {
            return "text/plain";
        }
        if (FilenameExtension.equalsIgnoreCase("vsd")) {
            return "application/vnd.visio";
        }
        if (FilenameExtension.equalsIgnoreCase("pptx") ||
                FilenameExtension.equalsIgnoreCase("ppt")) {
            return "application/vnd.ms-powerpoint";
        }
        if (FilenameExtension.equalsIgnoreCase("docx") ||
                FilenameExtension.equalsIgnoreCase("doc")) {
            return "application/msword";
        }
        if (FilenameExtension.equalsIgnoreCase("xml")) {
            return "text/xml";
        }
        return "image/jpeg";
    }

    /****
     *
     * @param argc
     */
    public static void main(String argc[]){
        try{
            /*Path path = Paths.get("/B+树.png");
            InputStream in = new FileInputStream(path.toFile());
            String accessUrl = uploadFile2OSS(path.toFile().getName(), in, "CONTENT_TYPE_TXT");
            System.out.println(accessUrl);*/
            System.out.println("-----------------------");
            System.out.println(ossClient.doesObjectExist(bucketName, "http://sunshinetest.oss-cn-beijing.aliyuncs.com/B%2B%E6%A0%91.png"));
            System.out.println("-----------------------");
            // ossClient.deleteObject(bucketName, "http://sunshinetest.oss-cn-beijing.aliyuncs.com/B%2B%E6%A0%91.png");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}


