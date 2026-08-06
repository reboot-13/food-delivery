package com.example.romanermilov.food_delivery_backend.exception

import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import com.example.romanermilov.food_delivery_backend.dto.error.ErrorResponse
import com.example.romanermilov.food_delivery_backend.dto.error.ValidationErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import java.time.LocalDateTime

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFoundException(exception: ProductNotFoundException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.NOT_FOUND
        val exceptionResponse = ErrorResponse(
            status.value(),
            status.reasonPhrase,
            exception.message!!,
            LocalDateTime.now().toString()
        )
        return ResponseEntity
            .status(status)
            .body(exceptionResponse)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
        fun handleValidationException(exception: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse>{
        val errors = exception.bindingResult
            .fieldErrors
            .mapNotNull { it.defaultMessage }

        val response = ValidationErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation failed",
            errors = errors,
            timestamp = LocalDateTime.now().toString()
        )
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response)
    }

    @ExceptionHandler(IncorrectAuthData::class)
    fun handleIncorrectAuthData(exception: IncorrectAuthData): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.UNAUTHORIZED
        val response = ErrorResponse(
            status = status.value(),
            error = "Unauthorized",
            message = exception.message!!,
            timestamp = LocalDateTime.now().toString()
        )

        return ResponseEntity
            .status(status)
            .body(response)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(exception: UserAlreadyExistsException) : ResponseEntity<ErrorResponse> {
        val status = HttpStatus.CONFLICT
        val response = ErrorResponse(
            status = status.value(),
            error = "User already exists",
            message = exception.message!!,
            timestamp = LocalDateTime.now().toString()
        )
        return ResponseEntity
            .status(status)
            .body(response)
    }
}