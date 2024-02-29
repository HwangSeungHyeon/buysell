package com.teamsparta.buysell.domain.exception

import com.teamsparta.buysell.domain.exception.dto.ErrorResponseDto
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SecurityException
import jakarta.security.auth.message.AuthException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.security.SignatureException

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(e: IllegalStateException): ResponseEntity<ErrorResponseDto>{
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponseDto(e.message))
    }

    //중복된 값이 있을 때 발생하는 에러
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolationException(e: DataIntegrityViolationException): ResponseEntity<ErrorResponseDto>{
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(ErrorResponseDto(e.message))
    }

    //validation 조건에 맞지 않을 경우 발생하는 에러
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ErrorResponseDto("${e.bindingResult.fieldErrors.first().defaultMessage}"))
    }

    //로그인 과정에서 에러가 발생할 경우 (아이디가 없거나, 비밀번호가 틀린 경우 등)
    @ExceptionHandler(BadCredentialsException::class)
    fun handleMethodBadCredentialsException(e: BadCredentialsException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponseDto(e.message))
    }

    //401 에러를 처리할 때 사용
    @ExceptionHandler(AuthException::class)
    fun handleAuthException(e: AuthException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponseDto(e.message))
    }

    //데이터베이스에서 못찾았을 때 사용
    @ExceptionHandler(ModelNotFoundException::class)
    fun handleModelNotFoundException(e: ModelNotFoundException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ErrorResponseDto(e.message))
    }

    @ExceptionHandler(SecurityException::class)
    fun handleSecurityException(e: SecurityException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponseDto(e.message))
    }

    //JWT 서명에 실패했을 때 발생하는 에러 (JWT가 변조되었을 때)
    @ExceptionHandler(SignatureException::class)
    fun handleSignatureException(e: SignatureException): ResponseEntity<ErrorResponseDto>{
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponseDto(e.message))
    }

    //구조적인 문제가 있는 JWT인 경우 발생하는 에러
    @ExceptionHandler(MalformedJwtException::class)
    fun handleMalformedJwtException(e: MalformedJwtException): ResponseEntity<ErrorResponseDto>{
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponseDto("옳바르지 않은 토큰입니다."))
    }

    //JWT의 유효기간이 초과되었을 때 발생하는 에러
    @ExceptionHandler(ExpiredJwtException::class)
    fun handleExpiredJwtException(e: ExpiredJwtException): ResponseEntity<ErrorResponseDto>{
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponseDto("토큰이 만료되었습니다. 다시 로그인해주세요."))
    }

    //수신한 JWT의 형식이 애플리케이션에서 원하는 형식과 맞지 않는 경우 발생하는 에러
    @ExceptionHandler(UnsupportedJwtException::class)
    fun handleUnsupportedJwtException(e: UnsupportedJwtException): ResponseEntity<ErrorResponseDto>{
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(ErrorResponseDto("JWT 구조가 다릅니다."))
    }
}