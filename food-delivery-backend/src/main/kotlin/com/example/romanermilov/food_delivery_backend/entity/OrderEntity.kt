package com.example.romanermilov.food_delivery_backend.entity

import com.example.romanermilov.food_delivery_backend.entity.enum.OrderStatus
import com.example.romanermilov.food_delivery_backend.entity.enum.OrderType
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class OrderEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity,

    @OneToMany(
        mappedBy = "order",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    var items: MutableList<OrderItemEntity> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: OrderStatus = OrderStatus.CREATED,

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    var orderType: OrderType,

    @Column(name = "pickup_point_id")
    var pickupPointId: Long? = null,

    @Column(name = "pickup_point_title", length = 100)
    var pickupPointTitle: String? = null,

    @Column(name = "pickup_point_address", length = 255)
    var pickupPointAddress: String? = null,

    @Column(name = "pickup_point_latitude", precision = 9, scale = 6)
    var pickupPointLatitude: BigDecimal? = null,

    @Column(name = "pickup_point_longitude", precision = 9, scale = 6)
    var pickupPointLongitude: BigDecimal? = null,

    @Column(name = "delivery_address", length = 255)
    var deliveryAddress: String?,

    @Column(name = "delivery_latitude", precision = 9, scale = 6)
    var deliveryLatitude: BigDecimal?,

    @Column(name = "delivery_longitude", precision = 9, scale = 6)
    var deliveryLongitude: BigDecimal?,


    @Column(name = "total_price", nullable = false)
    var totalPrice: BigDecimal = BigDecimal.ZERO,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
    )