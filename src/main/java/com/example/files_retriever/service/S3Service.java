package com.example.files_retriever.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class S3Service {

    private final AmazonS3 s3client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    public S3Service(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public List<String> searchFiles(String userName, String searchKey){
        String userPrefix = userName + "/";
        ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(userPrefix);
        ListObjectsV2Result result = s3client.listObjectsV2(request);

        return result.getObjectSummaries().stream()
                .map(S3ObjectSummary::getKey)
                .filter(key->key.contains(searchKey))
                .collect(Collectors.toList());
    }

    public String uploadFile(String filePath, InputStream inputStream, long size, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(contentType);
        metadata.setContentLength(size);

        PutObjectRequest request = new PutObjectRequest(bucketName, filePath, inputStream, metadata);
        s3client.putObject(request);

        return s3client.getUrl(bucketName, filePath).toString();
    }
}
