package com.example.romanermilov.food_delivery_backend.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "order_items")
class OrderItemEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    var order: OrderEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    var product: ProductEntity,

    @Column(nullable = false)
    var quantity: Int = 1,

    @Column(name = "product_name", nullable = false)
    var productName: String,

    @Column(name = "price_at_purchase", nullable = false)
    var priceAtPurchase: BigDecimal = BigDecimal.ZERO,

    @Column(name = "product_image_url")
    var productImageUrl: String? = null,
    )