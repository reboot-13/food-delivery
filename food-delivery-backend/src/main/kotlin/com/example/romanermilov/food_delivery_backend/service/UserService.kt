package com.example.romanermilov.food_delivery_backend.service

import com.example.romanermilov.food_delivery_backend.dto.request.AuthRequest
import com.example.romanermilov.food_delivery_backend.dto.request.RegisterRequest
import com.example.romanermilov.food_delivery_backend.dto.response.AuthResponse
import com.example.romanermilov.food_delivery_backend.entity.UserEntity
import com.example.romanermilov.food_delivery_backend.exception.IncorrectAuthData
import com.example.romanermilov.food_delivery_backend.exception.UserAlreadyExistsException
import com.example.romanermilov.food_delivery_backend.mapper.UserMapper
import com.example.romanermilov.food_delivery_backend.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {
    private fun existsByPhoneNumber(phoneNumber: String): Boolean {
        return userRepository.existsByPhoneNumber(phoneNumber)
    }

    private fun hashPassword(password: String): String {
        return requireNotNull(passwordEncoder.encode(password))
    }

    private fun passwordMatches(password: String, user: UserEntity) : Boolean {
        return passwordEncoder.matches(password, user.password)
    }

    fun findByPhoneNumber(phoneNumber: String): UserEntity? = userRepository.findByPhoneNumber(phoneNumber)


    private fun createAuthResponse(user: UserEntity, token: String): AuthResponse {
        val userResponse = UserMapper.toResponse(user)
        return AuthResponse(
            token = token,
            user = userResponse
        )
    }

    fun registerUser(request: RegisterRequest) : AuthResponse {
        if (existsByPhoneNumber(request.phoneNumber)){
            throw UserAlreadyExistsException(request.phoneNumber)
        }
        val password = hashPassword(request.password)
        val user = UserEntity(
            name = request.name,
            phoneNumber = request.phoneNumber,
            password = password
        )
        val savedUser = userRepository.save(user)
        val  token = jwtService.generateToken(savedUser)
        return createAuthResponse(user, token)
    }

    fun authUser(authRequest: AuthRequest) : AuthResponse {
            val user = userRepository.findByPhoneNumber(authRequest.phoneNumber) ?: throw IncorrectAuthData()

            if(!passwordMatches(authRequest.password, user)){
                throw IncorrectAuthData()
            }
            val token = jwtService.generateToken(user)

            return createAuthResponse(user, token)
        }
}