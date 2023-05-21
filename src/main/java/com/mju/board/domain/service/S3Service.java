package com.mju.board.domain.service;

import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    @Value("${cloud.aws.region.static}")
    private String region;
    @Value("${cloud.aws.s3.bucket[0]}")
    private String bucketNameBoard;

    @Value("${cloud.aws.s3.bucket[1]}")
    private String bucketNameBanner;
    private final S3Client s3Client;

    @Autowired
    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }
    public String uploadImageToS3Board(MultipartFile image) {
        return this.uploadImageToS3(image, bucketNameBoard);// iceamericano-board 버킷
    }

    public String uploadImageToS3Banner(MultipartFile image) {
        return this.uploadImageToS3(image, bucketNameBanner); // iceamericano-banner 버킷
    }
    public String uploadImageToS3(MultipartFile image, String bucketName) {
        String fileName = generateFileName(image);
        String imageUrl = null;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .acl("public-read")
                    .contentType(image.getContentType())
                    .contentLength(image.getSize())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(image.getInputStream(), image.getSize()));

            imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUrl;
    }
    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID().toString() + "_" + file.getOriginalFilename();//고유 번호 _ 파일이름
    }

    public void deleteImageFromS3Board(String imageUrl) {
        if (!StringUtils.isEmpty(imageUrl)) {
            String fileName = imageUrl.split("/")[3];
            s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketNameBoard).key(fileName).build());
        }
    }

    public void deleteImageFromS3Banner(String imageUrl) {
        if (!StringUtils.isEmpty(imageUrl)) {
            String fileName = imageUrl.split("/")[3];
            s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketNameBanner).key(fileName).build());
        }
    }
}