package com.teamsparta.buysell.infra.aws

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
class AwsS3Config {
    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .region(Region.AP_NORTHEAST_2)
            .build()
    }

    @Bean
    fun presigner(): S3Presigner {
        return S3Presigner.builder()
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .region(Region.AP_NORTHEAST_2)
            .build()
    }
}