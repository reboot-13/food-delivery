package com.example.fooddeliveryandroid.domain.mapper

import com.example.fooddeliveryandroid.data.remote.dto.response.OrderItemResponse
import com.example.fooddeliveryandroid.data.remote.dto.response.OrderResponse
import com.example.fooddeliveryandroid.data.remote.dto.request.CreateOrderItemRequest
import com.example.fooddeliveryandroid.domain.model.CartItem
import com.example.fooddeliveryandroid.domain.model.Order
import com.example.fooddeliveryandroid.domain.model.OrderItem
import java.time.LocalDateTime

object OrderMapper {
    fun responseToModel(response: OrderResponse): Order {

        return Order(
            id = response.id,
            userId = response.userId,
            items = response.items.map { item ->
                orderItemResponseToModel(item)
            },
            createdAt = LocalDateTime.parse(response.createdAt),
            status = response.status,
            orderType = response.orderType,

            deliveryAddress = response.deliveryAddress,
            deliveryLatitude = response.deliveryLatitude,
            deliveryLongitude = response.deliveryLongitude,

            pickupPointId = response.pickupPointId,
            pickupPointTitle = response.pickupPointTitle,
            pickupPointAddress = response.pickupPointAddress,
            pickupPointLatitude = response.pickupPointLatitude,
            pickupPointLongitude = response.pickupPointLongitude,

            totalPrice = response.totalPrice
        )
    }

    fun orderItemResponseToModel(response: OrderItemResponse): OrderItem {
        return OrderItem(
            id = response.id,
            productId = response.productId,
            quantity = response.quantity,
            priceAtPurchase = response.priceAtPurchase,
            productName = response.productName,
            productImageUrl = response.productImageUrl
        )
    }


    fun cartItemToOrderItemRequest (cartItem: CartItem): CreateOrderItemRequest {
        return CreateOrderItemRequest(
            productId = cartItem.product.id,
            quantity = cartItem.quantity
        )
    }
}