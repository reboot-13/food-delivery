package com.example.romanermilov.food_delivery_backend.controller

import com.example.romanermilov.food_delivery_backend.dto.response.ProductResponse
import com.example.romanermilov.food_delivery_backend.dto.response.ShortProductResponse
import com.example.romanermilov.food_delivery_backend.service.ProductService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
class ProductController (val productService: ProductService) {
    @GetMapping
    fun getProducts(@RequestParam(required = false) categoryId: Long?): List<ProductResponse> {
        return if (categoryId == null) productService.getAllProducts()
        else productService.getAllProductsByCategoryId(categoryId)
    }
    @GetMapping("/{productId}")
    fun getProductById(@PathVariable productId: Long): ProductResponse {
        return productService.getProductById(productId)
    }


}

