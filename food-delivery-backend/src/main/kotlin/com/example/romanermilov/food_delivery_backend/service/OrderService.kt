package com.example.romanermilov.food_delivery_backend.service

import com.example.romanermilov.food_delivery_backend.dto.request.CreateOrderRequest
import com.example.romanermilov.food_delivery_backend.dto.response.CreateOrderItemRequest
import com.example.romanermilov.food_delivery_backend.dto.response.OrderResponse
import com.example.romanermilov.food_delivery_backend.entity.OrderEntity
import com.example.romanermilov.food_delivery_backend.entity.OrderItemEntity
import com.example.romanermilov.food_delivery_backend.entity.ProductEntity
import com.example.romanermilov.food_delivery_backend.entity.UserEntity
import com.example.romanermilov.food_delivery_backend.entity.enum.UserRole
import com.example.romanermilov.food_delivery_backend.exception.IncorrectAuthData
import com.example.romanermilov.food_delivery_backend.exception.NotEnoughAccessRights
import com.example.romanermilov.food_delivery_backend.exception.OrderIsEmptyException
import com.example.romanermilov.food_delivery_backend.exception.OrderNotFoundException
import com.example.romanermilov.food_delivery_backend.exception.ProductNotFoundException
import com.example.romanermilov.food_delivery_backend.exception.ProductUnavailableException
import com.example.romanermilov.food_delivery_backend.mapper.OrderMapper
import com.example.romanermilov.food_delivery_backend.repository.OrderRepository
import com.example.romanermilov.food_delivery_backend.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.math.BigDecimal
import kotlin.collections.component1
import kotlin.collections.component2

@Service
class OrderService (
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
) {
    private fun getUser(): UserEntity {
        val authentication = SecurityContextHolder.getContext().authentication
        val user = authentication?.principal as? UserEntity
            ?: throw IncorrectAuthData()
        return user
    }

    private fun calculateTotalPrice(items: List<OrderItemEntity>): BigDecimal {
        var  total: BigDecimal = BigDecimal.ZERO
        for (item in items) {
            total += item.priceAtPurchase * item.quantity.toBigDecimal()
        }
        return total
    }

    private fun mergeDuplicateItems(items: List<CreateOrderItemRequest>): List<CreateOrderItemRequest> {
        val itemsWithoutDuplicates = items
            .groupBy { it.productId }
            .map { (productId, items) ->
                CreateOrderItemRequest(
                    productId = productId,
                    quantity = items.sumOf { it.quantity }
                )
            }
        return itemsWithoutDuplicates
    }

    private fun getAvailableProduct(productId: Long) : ProductEntity {
        val product = productRepository.findById(productId)
            .orElseThrow{
                ProductNotFoundException(productId)
            }
        if(!product.available) {
            throw ProductUnavailableException(product.name)
        }
        return product
    }

    private fun createOrderItems(request: CreateOrderRequest, order: OrderEntity): List<OrderItemEntity> {

        val orderItems = mergeDuplicateItems(request.items)
            .map { orderItem ->
                val product = getAvailableProduct(orderItem.productId)
                OrderItemEntity(
                    order = order,
                    product = product,
                    quantity = orderItem.quantity,
                    productName = product.name,
                    priceAtPurchase = product.price,
                    productImageUrl = product.imageUrl,
                )
        }
        return orderItems
    }
    @Transactional
    fun createOrder(request: CreateOrderRequest): OrderResponse {
        if (request.items.isEmpty()) {
            throw OrderIsEmptyException()
        }
        val user = getUser()
        val order = OrderEntity(
            user = user
        )
        order.items = createOrderItems(request, order).toMutableList()
        order.totalPrice = calculateTotalPrice(order.items)
        orderRepository.save(order)

        return OrderMapper.orderToResponse(order)
    }

    @Transactional
    fun getOrdersByUser(): List<OrderResponse> {
        val ordersEntity = orderRepository.findOrdersByUser(getUser())
        return ordersEntity.map { OrderMapper.orderToResponse(it) }
    }

    private fun checkOrderAccess(
        order: OrderEntity,
        user: UserEntity
    ) {
        val isOwner = user.id == order.user.id
        val isAdmin = user.role == UserRole.ADMIN
        if (!isOwner && !isAdmin) throw NotEnoughAccessRights()
    }

    @Transactional
    fun getOrderById(id: Long): OrderResponse {
        val user = getUser()
        val order = orderRepository.findById(id)
            .orElseThrow { OrderNotFoundException()}
        checkOrderAccess(order, user)
        return OrderMapper.orderToResponse(order)
    }
}