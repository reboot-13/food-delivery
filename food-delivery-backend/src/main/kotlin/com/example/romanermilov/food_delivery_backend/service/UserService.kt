package com.example.romanermilov.food_delivery_backend.service

import com.example.romanermilov.food_delivery_backend.dto.request.AuthRequest
import com.example.romanermilov.food_delivery_backend.dto.request.RegisterRequest
import com.example.romanermilov.food_delivery_backend.dto.request.UpdateUserRequest
import com.example.romanermilov.food_delivery_backend.dto.response.AuthResponse
import com.example.romanermilov.food_delivery_backend.dto.response.UserResponse
import com.example.romanermilov.food_delivery_backend.entity.UserEntity
import com.example.romanermilov.food_delivery_backend.exception.IncorrectAuthData
import com.example.romanermilov.food_delivery_backend.exception.PhoneNumberAlreadyExistsException
import com.example.romanermilov.food_delivery_backend.exception.UserAlreadyExistsException
import com.example.romanermilov.food_delivery_backend.exception.UserByIdNotFoundException
import com.example.romanermilov.food_delivery_backend.exception.UserNotFoundException
import com.example.romanermilov.food_delivery_backend.mapper.AddressMapper
import com.example.romanermilov.food_delivery_backend.mapper.UserMapper
import com.example.romanermilov.food_delivery_backend.repository.AddressRepository
import com.example.romanermilov.food_delivery_backend.repository.UserRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val addressRepository: AddressRepository,
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

    fun findByUserId(userId: Long): Optional<UserEntity> {
        return userRepository.findById(userId)
    }

    fun findByPhoneNumber(phoneNumber: String): UserEntity {
        return userRepository
            .findByPhoneNumber(phoneNumber)
            .orElseThrow{UserNotFoundException(phoneNumber)}
    }

    private fun getCurrentUser(): UserEntity {
        val authentication = SecurityContextHolder
            .getContext()
            .authentication

        val userId = (authentication?.principal as? UserEntity)?.id
            ?: throw IncorrectAuthData()

        return userRepository.findById(userId)
            .orElseThrow {
                UserByIdNotFoundException(userId)
            }
    }

    fun getUser(): UserResponse {
        val user = getCurrentUser()
        val addresses = addressRepository.findAllByUserId(requireNotNull(user.id))
            .map(AddressMapper::toResponse)
        return UserMapper.toResponse(user, addresses)
    }


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
            val user = findByPhoneNumber(authRequest.phoneNumber)

            if(!passwordMatches(authRequest.password, user)){
                throw IncorrectAuthData()
            }
            val token = jwtService.generateToken(user)

            return createAuthResponse(user, token)
        }

    @Transactional
    fun updateCurrentUser(
        request: UpdateUserRequest
    ): UserResponse {
        val user = getCurrentUser()

        if (
            request.phoneNumber != user.phoneNumber &&
            userRepository.existsByPhoneNumberAndIdNot(
                request.phoneNumber,
                user.id!!
            )
        ) {
            throw PhoneNumberAlreadyExistsException(user.phoneNumber)
        }

        user.name = request.name
        user.phoneNumber = request.phoneNumber

        val savedUser = userRepository.save(user)
        val addresses = addressRepository.findAllByUserId(user.id!!)

        return UserMapper.toResponse(
            user = savedUser,
            addresses = addresses.map {
                AddressMapper.toResponse(it)
            })
    }
}