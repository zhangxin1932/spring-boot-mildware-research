package com.zy.spring.mildware.files.controller;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * https://docs.min.io/cn/java-client-quickstart-guide.html
 *
 * Detected default credentials 'minioadmin:minioadmin',
 * please change the credentials immediately
 * using 'MINIO_ACCESS_KEY' and 'MINIO_SECRET_KEY'
 */
@RestController
@RequestMapping(value = "/minIO/File")
@Slf4j
public class MinIOFileController {

    @Autowired
    private MinioClient minioClient;

    public String upload(@RequestParam("file") MultipartFile file) {
        /*BucketExistsArgs args = BucketExistsArgs.builder().bucket("minio-bucket01").build();
        if (!minioClient.bucketExists(args)) {
            MakeBucketArgs bucketArgs = MakeBucketArgs.builder().bucket("minio-bucket01").build();
            minioClient.makeBucket(bucketArgs);
        }
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket(args.bucket())
                .stream()
                .build();
        minioClient.putObject();*/

        return null;
    }
}
