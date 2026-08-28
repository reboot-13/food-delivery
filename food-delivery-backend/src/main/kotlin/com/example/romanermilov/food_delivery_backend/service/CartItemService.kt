package com.example.romanermilov.food_delivery_backend.service

import com.example.romanermilov.food_delivery_backend.dto.request.AddCartItemRequest
import com.example.romanermilov.food_delivery_backend.dto.response.CartItemResponse
import com.example.romanermilov.food_delivery_backend.entity.CartItemEntity
import com.example.romanermilov.food_delivery_backend.entity.ProductEntity
import com.example.romanermilov.food_delivery_backend.entity.UserEntity
import com.example.romanermilov.food_delivery_backend.exception.CartItemAlreadyExistsException
import com.example.romanermilov.food_delivery_backend.exception.CartItemNotFoundException
import com.example.romanermilov.food_delivery_backend.exception.ProductNotFoundException
import com.example.romanermilov.food_delivery_backend.mapper.CartItemMapper
import com.example.romanermilov.food_delivery_backend.repository.CartItemRepository
import com.example.romanermilov.food_delivery_backend.repository.ProductRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.example.romanermilov.food_delivery_backend.exception.NoItemInCartException

@Service
class CartItemService (
    private val cartItemRepository: CartItemRepository,
    private val productRepository: ProductRepository
){
    fun addItem(addCartItemRequest: AddCartItemRequest): CartItemResponse {
        val user = getUser()
        val product = getProductById(addCartItemRequest.productId)

        val alreadyExists = cartItemRepository
            .findByProductIdAndUserId(
            userId = user.id!!,
            productId = product.id!!
        )
        if (alreadyExists.isPresent) {
            throw CartItemAlreadyExistsException(product.id!!)
        }

        val cartItem = CartItemEntity(
            user = user,
            product = product,
            quantity = 1
        )

        val savedCartItem = cartItemRepository.save(cartItem)
        return CartItemMapper.toResponse(savedCartItem)
    }

    @Transactional
    fun updateQuantity(productId: Long, quantity: Short): CartItemResponse {
        val user = getUser()
        val cartItem = cartItemRepository
            .findByProductIdAndUserId(productId, user.id!!)
            .orElseThrow {
                CartItemNotFoundException(productId)
            }

        cartItem.quantity = quantity
        val savedCartItem = cartItemRepository.save(cartItem)
        return CartItemMapper.toResponse(savedCartItem)
    }

    @Transactional
    fun deleteItem(productId: Long) {
        val user = getUser()
        cartItemRepository
            .findByProductIdAndUserId(productId, user.id!!)
            .orElseThrow {
                NoItemInCartException(productId)
            }
        cartItemRepository.deleteByUserIdAndProductId(userId = user.id!!, productId = productId)
    }

    @Transactional(readOnly = true)
    fun getCartItems(): List<CartItemResponse> {
        val user = getUser()
        return cartItemRepository.findByUserId(user.id!!).map {
            CartItemMapper.toResponse(it)
        }
    }

    private fun getProductById(productId: Long): ProductEntity {
        return productRepository.findById(productId).orElseThrow { ProductNotFoundException(productId) }
    }

    private fun getUser(): UserEntity {
    return SecurityContextHolder.getContext().authentication?.principal as UserEntity
    }
}