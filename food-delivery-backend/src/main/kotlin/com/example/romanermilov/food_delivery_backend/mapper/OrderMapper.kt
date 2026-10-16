package com.example.romanermilov.food_delivery_backend.mapper

import com.example.romanermilov.food_delivery_backend.dto.response.OrderItemResponse
import com.example.romanermilov.food_delivery_backend.dto.response.OrderResponse
import com.example.romanermilov.food_delivery_backend.entity.OrderEntity
import com.example.romanermilov.food_delivery_backend.entity.OrderItemEntity

object OrderMapper{
    fun orderItemToOrderItemResponse(orderItem: OrderItemEntity): OrderItemResponse {
        return OrderItemResponse(
            id = orderItem.id!!,
            productId = orderItem.product.id!!,
            productName = orderItem.productName,
            productImageUrl = orderItem.productImageUrl,
            priceAtPurchase = orderItem.priceAtPurchase,
            quantity = orderItem.quantity,
        )
    }

    fun orderToResponse(order: OrderEntity): OrderResponse {
        val orderItemsResponse = order.items.map { orderItemToOrderItemResponse(it) }
        return OrderResponse(
            id = order.id!!,
            userId = order.user.id!!,
            items = orderItemsResponse,
            status = order.status,
            orderType = order.orderType,
            totalPrice = order.totalPrice,

            deliveryAddress = order.deliveryAddress,
            deliveryLatitude = order.deliveryLatitude,
            deliveryLongitude = order.deliveryLongitude,

            pickupPointId = order.pickupPointId,
            pickupPointTitle = order.pickupPointTitle,
            pickupPointAddress = order.pickupPointAddress,
            pickupPointLatitude = order.pickupPointLatitude,
            pickupPointLongitude = order.pickupPointLongitude,

            createdAt = order.createdAt
        )
    }
}