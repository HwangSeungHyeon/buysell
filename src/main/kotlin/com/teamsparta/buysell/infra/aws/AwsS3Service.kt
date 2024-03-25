package com.teamsparta.buysell.infra.aws

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.io.IOException
import java.util.UUID
import kotlin.jvm.Throws

@Service
class AwsS3Service(
    private val amazonS3Client: AmazonS3Client
) {
    @Value("\${cloud.aws.s3.bucket}")
    lateinit var bucket: String

    @Throws(IOException::class)
    fun uploadImage(multipartField: List<MultipartFile>?): List<String> {
        val fileNameList = mutableListOf<String>()

        multipartField!!.forEach { file ->
            val fileName = UUID.randomUUID().toString() + "-" + file.originalFilename
            val objectMetadata = ObjectMetadata().apply {
                contentLength = file.size
                contentType = file.contentType
            }
            try {
                file.inputStream.use { inputStream ->
                    amazonS3Client.putObject(
                        PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead)
                    )
                }
            } catch (e: IOException) {
                throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.")
            }
            fileNameList.add("https://test-buysell.s3.ap-northeast-2.amazonaws.com/$fileName")
        }
        return fileNameList
    }
}