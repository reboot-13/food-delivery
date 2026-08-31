package com.example.romanermilov.food_delivery_backend.service

import com.example.romanermilov.food_delivery_backend.dto.response.ProductResponse
import com.example.romanermilov.food_delivery_backend.dto.response.ShortProductResponse
import com.example.romanermilov.food_delivery_backend.exception.ProductNotFoundException
import com.example.romanermilov.food_delivery_backend.mapper.ProductMapper
import com.example.romanermilov.food_delivery_backend.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ProductService (
    private val productRepository: ProductRepository
){
    @Transactional
    fun getAllProducts(): List<ProductResponse>{
        return productRepository.findByAvailableTrue()
            .map(ProductMapper::toResponse)
    }
    @Transactional
    fun getAllProductsByCategoryId(categoryId: Long): List<ProductResponse>{
        return productRepository.findByCategoryIdAndAvailableTrue(categoryId)
            .map(ProductMapper::toResponse)
    }

    @Transactional
    fun getProductById(id: Long): ProductResponse {
        val productResponse = productRepository
            .findByIdAndAvailableTrue(id)
            .orElseThrow { ProductNotFoundException(id) }
            .let(ProductMapper::toResponse)
        return productResponse
    }
}