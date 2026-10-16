package com.example.fooddeliveryandroid.domain.model.enums

enum class OrderStatus(
    val description: String,
    val specification: String) {
    CREATED(
        description = "Принят",
        specification = "Приняли заказ"),
    COOKING(
        description = "Готовим",
        specification = "Уже готовим"
    ),
    DELIVERING(
        description = "Везём",
        specification = "Везём!"
    ),
    COMPLETED(
        description = "Доставлен",
        "Доставили!"
    ), CANCELLED(
        description = "Заказ отменён",
        specification = "")
}