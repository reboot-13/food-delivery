package com.example.romanermilov.food_delivery_backend.filter

import com.example.romanermilov.food_delivery_backend.service.JwtService
import com.example.romanermilov.food_delivery_backend.service.UserService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.coyote.BadRequestException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userService: UserService
) : OncePerRequestFilter(){

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.servletPath

        return path.startsWith("/products") ||
                path.startsWith("/categories") ||
                path == "/users/register" ||
                path == "/users/auth"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if(SecurityContextHolder.getContext().authentication == null) {
            val authHeader = request.getHeader("Authorization")
            if (authHeader != null && authHeader.startsWith("Bearer ")){
                val receivedToken = authHeader.substring(7)
                try {
                    val userId = jwtService.extractUserId(receivedToken)

                    val user = userService.findByUserId(userId)
                        .orElse(null)

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

                } catch (e: ExpiredJwtException) {
                    response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "JWT expired"
                    )
                    return
                } catch (e: JwtException) {
                    response.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Invalid JWT"
                    )
                    return
                } catch (e: BadRequestException) {
                    response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Bad request"
                    )
                }
            }
        }
        filterChain.doFilter(request, response)
    }
}