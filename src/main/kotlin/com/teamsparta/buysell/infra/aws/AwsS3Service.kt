package com.teamsparta.buysell.infra.aws

import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class AwsS3Service(
    private val amazonS3: AmazonS3
) {
    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String

    private fun createPath(prefix: String, fileName: String): String {
        val fileId = createFieldId()

        return "$prefix/$fileId$fileName"
    }

    fun getPreSignedUrl(prefix: String?, fileName: String): String {
        var filePath = fileName
        if (!prefix.isNullOrBlank()) {
            filePath = createPath(prefix, fileName)
        }

        val generatePreSignedUrlRequest = getGeneratePrSignedUrlRequest(bucket, filePath)
        val url = amazonS3.generatePresignedUrl(generatePreSignedUrlRequest)

        return url.toString()
    }

    private fun getPreSignedUrlExpiration(): Date {
        val expiration = Date()
        expiration.time += 1000 * 60 * 2 // 2분

        return expiration
    }

    private fun getGeneratePrSignedUrlRequest(bucket: String, fileName: String): GeneratePresignedUrlRequest {
        val expiration = getPreSignedUrlExpiration()

        val generatePreSignedUrlRequest = GeneratePresignedUrlRequest(bucket, fileName)
            .withMethod(HttpMethod.PUT)
            .withExpiration(expiration)
        generatePreSignedUrlRequest.addRequestParameter("x-amz-acl", CannedAccessControlList.PublicRead.toString())

        return generatePreSignedUrlRequest
    }

    private fun createFieldId(): String {

        return UUID.randomUUID().toString()
    }
}

