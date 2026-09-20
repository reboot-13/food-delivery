package com.example.fooddeliveryandroid.domain.model.enums

enum class OrderStatus(val description: String) {
    CREATED("Принят"), COOKING("Готовим"),  DELIVERING("Везём"), COMPLETED("Доставлен"), CANCELLED("Заказ отменён")
}