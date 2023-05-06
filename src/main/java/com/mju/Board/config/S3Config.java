package com.mju.Board.config;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return ProfileCredentialsProvider.builder()
                .profileName("BoardService")  // 사용할 프로필 이름 지정
                .build();
    }
    @Bean
    public S3Client s3Client() {
//        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create("", "");

        return S3Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(awsCredentialsProvider())
                .build();
    }

}
