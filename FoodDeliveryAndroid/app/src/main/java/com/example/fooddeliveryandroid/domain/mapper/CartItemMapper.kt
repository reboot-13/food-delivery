package com.example.fooddeliveryandroid.domain.mapper

import com.example.fooddeliveryandroid.data.local.entity.CartItemEntity
import com.example.fooddeliveryandroid.data.local.room.relation.CartItemWithProduct
import com.example.fooddeliveryandroid.data.remote.dto.CartItemResponse
import com.example.fooddeliveryandroid.data.remote.dto.ProductCartItemResponse
import com.example.fooddeliveryandroid.domain.model.CartItem
import com.example.fooddeliveryandroid.domain.model.Category
import com.example.fooddeliveryandroid.domain.model.Product

object CartItemMapper {
    fun responseToModel (response: CartItemResponse): CartItem {
        val product = ProductCartItemResponse(
            id = response.product.id,
            name = response.product.name,
            price = response.product.price,
            imageUrl = response.product.imageUrl,
            available = response.product.available
        )
        return CartItem(
            product = ProductMapper.responseToModel(product),
            quantity = response.quantity
        )
    }

    fun cartItemWithProductToModel(cartItemWithProduct: CartItemWithProduct): CartItem {
        return CartItem(
            product = Product(
                id = cartItemWithProduct.productId,
                name = cartItemWithProduct.productName,
                price = cartItemWithProduct.price,
                imageUrl = cartItemWithProduct.imageUrl,
                weight = cartItemWithProduct.weight,
                available = cartItemWithProduct.available,
                category = Category(
                    id = cartItemWithProduct.categoryId,
                    name = cartItemWithProduct.categoryName
                )
            ),
            quantity = cartItemWithProduct.quantity
        )
    }
    fun responseToEntity (response: CartItemResponse): CartItemEntity {
        return CartItemEntity(productId = response.product.id, quantity = response.quantity)
    }

}