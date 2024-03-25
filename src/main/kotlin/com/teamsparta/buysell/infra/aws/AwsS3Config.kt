package com.teamsparta.buysell.infra.aws

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsS3Config {
    @Value("\${cloud.aws.credentials.access-key")
    val accessKey: String? = null

    @Value("\${cloud.aws.credentials.secret-key")
    val secretKey: String? = null

    @Value("\${cloud.aws.region.static")
    val region: String? = null

    @Bean
    fun awsCredentialProvider(): BasicAWSCredentials {
        return BasicAWSCredentials(accessKey, secretKey)
    }

    @Bean
    fun amazonS3(): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentialProvider()))
            .build()

    }
}