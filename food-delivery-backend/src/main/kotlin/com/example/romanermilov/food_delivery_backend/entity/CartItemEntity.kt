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

@Entity
@Table(name = "cart_items")
class CartItemEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn("user_id", nullable = false)
    var user: UserEntity,

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn("product_id", nullable = false)
    var product: ProductEntity,

    @Column(name = "quantity", nullable = false)
    var quantity: Short,
)