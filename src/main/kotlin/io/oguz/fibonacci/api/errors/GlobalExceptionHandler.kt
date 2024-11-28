package io.oguz.fibonacci.api.errors

import io.github.oshai.kotlinlogging.KotlinLogging
import io.oguz.fibonacci.domain.exceptions.EntityOutOfAllowedRangeException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.multipart.support.MissingServletRequestPartException


@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(
        MissingServletRequestParameterException::class,
        MissingServletRequestPartException::class,
        HttpMessageNotReadableException::class,
        IllegalArgumentException::class,
        IllegalStateException::class,
        ConstraintViolationException::class,
        EntityOutOfAllowedRangeException::class
    )
    private fun handleBadRequest(ex: Exception): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiError(
                HttpStatus.BAD_REQUEST.value(),
                ex.localizedMessage
            )
        )

    @ExceptionHandler(value = [OutOfMemoryError::class])
    fun handleOutOfMemoryError(ex: OutOfMemoryError): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Request failed with OutOfMemoryError"
            )
        ).also {
            logger.error(ex) { ex.message }
        }

    @ExceptionHandler(value = [StackOverflowError::class])
    fun handleStackOverflowError(ex: StackOverflowError): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Request failed with StackOverflowError"
            )
        ).also {
            logger.error(ex) { ex.message }
        }
}
