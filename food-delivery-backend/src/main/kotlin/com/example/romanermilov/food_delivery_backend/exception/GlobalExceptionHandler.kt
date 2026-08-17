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

    @ExceptionHandler(NotEnoughAccessRights::class)
    fun handleNotEnoughAccessRights(exception: NotEnoughAccessRights): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.FORBIDDEN
        val exceptionResponse = ErrorResponse(
            status = status.value(),
            error = "Not enough access rights",
            message = exception.message!!,
            timestamp = LocalDateTime.now().toString()
        )
        return ResponseEntity
            .status(status)
            .body(exceptionResponse)
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(exception: UserNotFoundException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.NOT_FOUND
        val exceptionResponse = ErrorResponse(
            status = status.value(),
            error = "User not found",
            message = exception.message!!,
            timestamp = LocalDateTime.now().toString()
        )
        return ResponseEntity
            .status(status)
            .body(exceptionResponse)
    }

    @ExceptionHandler(OrderNotFoundException::class)
    fun handleOrderNotFoundException(exception: OrderNotFoundException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.NOT_FOUND
        val exceptionResponse = ErrorResponse(
            status = status.value(),
            error = "Order not found",
            message = exception.message!!,
            timestamp = LocalDateTime.now().toString()
        )
        return ResponseEntity
            .status(status)
            .body(exceptionResponse)
    }

    @ExceptionHandler (CartItemAlreadyExistsException::class)
    fun handleCartItemAlreadyExistsException(exception: CartItemAlreadyExistsException): ResponseEntity<ErrorResponse> {
        val status = HttpStatus.CONFLICT
        val exceptionResponse = ErrorResponse(
            status = status.value(),
            error = "CartItem already exists",
            message = exception.message!!,
            timestamp = LocalDateTime.now().toString()
        )
        return ResponseEntity
            .status(status)
            .body(exceptionResponse)
    }
}