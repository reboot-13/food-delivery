package com.example.romanermilov.food_delivery_backend.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "addresses")
class AddressEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false
    )
    val user: UserEntity,

    @Column(length = 50)
    val title: String? = null,

    @Column(nullable = false, length = 100)
    val city: String,

    @Column(nullable = false, length = 150)
    val street: String,

    @Column(nullable = false, length = 20)
    val house: String,

    @Column(length = 20)
    val apartment: String? = null,

    @Column(length = 20)
    val entrance: String? = null,

    val floor: Short? = null,

    @Column(length = 20)
    val intercom: String? = null,

    @Column(columnDefinition = "TEXT")
    val comment: String? = null,

    @Column(nullable = false, precision = 9, scale = 6)
    val latitude: BigDecimal,

    @Column(nullable = false, precision = 9, scale = 6)
    val longitude: BigDecimal
)