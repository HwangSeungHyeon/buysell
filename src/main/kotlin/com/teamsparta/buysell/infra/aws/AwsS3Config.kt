package com.teamsparta.buysell.infra.aws

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class AwsS3Config {
    @Value("\${cloud.aws.credentials.access-key")
    val accessKey: String? = null

    @Value("\${cloud.aws.credentials.secret-key")
    val secretKey: String? = null

    @Value("\${cloud.aws.region.static")
    val region: String? = null

    @Bean
    fun AmazonS3Client(): AmazonS3Client {
        val awsCred: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        return AmazonS3ClientBuilder.standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(awsCred))
            .build() as AmazonS3Client
    }
}