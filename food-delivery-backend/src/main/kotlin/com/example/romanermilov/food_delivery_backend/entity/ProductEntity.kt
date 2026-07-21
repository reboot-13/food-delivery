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
@Table(name = "products")
class ProductEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false)
    var name: String = "",
    var description: String? = null,
    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO,
    @Column(name = "image_url")
    var imageUrl: String? = null,
    var weight: Short? = null,
    var calories: Short? = null,
    @Column(nullable = false)
    var available: Boolean = true,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: CategoryEntity
)