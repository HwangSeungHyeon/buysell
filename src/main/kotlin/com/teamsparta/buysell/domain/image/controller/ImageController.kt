package com.teamsparta.buysell.domain.image.controller

import com.teamsparta.buysell.domain.image.service.AwsS3Service
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.IOException

@RestController
@RequestMapping("/images")
class ImageController(
    private val awsS3Service: AwsS3Service
) {
    @GetMapping("/posts")
    @Throws(IOException::class)
    fun putFile(
        @RequestParam fileName: String
    ): ResponseEntity<String> {
        val url = awsS3Service.putPreSignUrl(fileName)

        return ResponseEntity(url, HttpStatus.OK)
    }

    @GetMapping("/views")
    @Throws(IOException::class)
    fun getFile(
        @RequestParam fileName: String
    ): ResponseEntity<String> {
        val url = awsS3Service.getPreSignedImageUrl(fileName)

        return if (url != null) {
            ResponseEntity(url, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }
}