package com.example.romanermilov.food_delivery_backend.mapper

import com.example.romanermilov.food_delivery_backend.dto.response.OrderItemResponse
import com.example.romanermilov.food_delivery_backend.dto.response.OrderResponse
import com.example.romanermilov.food_delivery_backend.entity.OrderEntity
import com.example.romanermilov.food_delivery_backend.entity.OrderItemEntity

object OrderMapper{
    fun orderItemToOrderItemResponse(orderItem: OrderItemEntity): OrderItemResponse {
        return OrderItemResponse(
            orderItem.id!!,
            orderItem.product.id!!,
            orderItem.productName,
            orderItem.productImageUrl,
            orderItem.priceAtPurchase,
            orderItem.quantity,
        )
    }

    fun orderToResponse(order: OrderEntity): OrderResponse {
        val orderItemsResponse = order.items.map { orderItemToOrderItemResponse(it) }
        return OrderResponse(
            order.id!!,
            order.user.id!!,
            items = orderItemsResponse,
            order.status,
            order.totalPrice,
            order.createdAt
        )
    }
}