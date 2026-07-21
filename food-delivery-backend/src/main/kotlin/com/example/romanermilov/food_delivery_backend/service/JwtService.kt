package com.example.romanermilov.food_delivery_backend.service

import com.example.romanermilov.food_delivery_backend.entity.UserEntity
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService {
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    @Value("\${jwt.expiration}")
    private var expiration: Long = 0

    private fun getSigningKey(): SecretKey {
        val keyBytes = secret.toByteArray()
        val secretKey = Keys.hmacShaKeyFor(keyBytes)
        return secretKey
    }

    fun generateToken(user: UserEntity): String {
        val now = Date()
        val expirationDate = Date(now.time + expiration)
        return Jwts.builder()
            .subject(user.phoneNumber)
            .claim("role", user.role.name)
            .issuedAt(now)
            .expiration(expirationDate)
            .signWith(getSigningKey())
            .compact()
    }

    fun extractPhoneNumber(token: String): String {
        return extractAllClaims(token).subject
    }

    fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    fun extractRole(token: String): String {
        return extractAllClaims(token)
            .get("role", String::class.java)
    }
}