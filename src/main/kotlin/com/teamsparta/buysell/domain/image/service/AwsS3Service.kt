package com.teamsparta.buysell.domain.image.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.time.Duration


@Service
class AwsS3Service(
    private val s3Client: S3Client,
    private val presigner: S3Presigner,
    @Value("\${cloud.aws.s3.bucket}")
    private var bucketName: String,
) {
    fun putPreSignUrl(fileName: String?): String? {
        if (fileName.isNullOrBlank()) {
            return null
        }

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(fileName)
            .build()

        val putObjectPresignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(5))
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedPutObjectRequest = presigner.presignPutObject(putObjectPresignRequest)
        val url = presignedPutObjectRequest.url().toString()

        return url
    }

     fun getPreSignedImageUrl(fileName: String?): String? {
         if (fileName.isNullOrBlank()) {
             return null
         }

         val getObjectRequest = GetObjectRequest.builder()
             .bucket(bucketName)
             .key(fileName)
             .build()

         val getObjectPresignedRequest = GetObjectPresignRequest.builder()
             .signatureDuration(Duration.ofMinutes(5))
             .getObjectRequest(getObjectRequest)
             .build()

         val presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignedRequest)
         val url = presignedGetObjectRequest.url().toString()

         return url
     }
}

