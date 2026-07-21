package com.example.romanermilov.food_delivery_backend.filter

import com.example.romanermilov.food_delivery_backend.service.JwtService
import com.example.romanermilov.food_delivery_backend.service.UserService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userService: UserService
) : OncePerRequestFilter(){

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if(SecurityContextHolder.getContext().authentication == null) {
            val authHeader = request.getHeader("Authorization")
            if (authHeader != null && authHeader.startsWith("Bearer ")){
                val receivedToken = authHeader.substring(7)
                val phoneNumber = jwtService.extractPhoneNumber(receivedToken)
                val user = userService.findByPhoneNumber(phoneNumber)
                if (user != null) {
                    val authentication = UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        emptyList()
                    )
                    SecurityContextHolder
                        .getContext()
                        .authentication = authentication
                }

            }
        }
        filterChain.doFilter(request, response)
    }
}