package com.example.fooddeliveryandroid.data.repository.cartSource

import com.example.fooddeliveryandroid.data.local.entity.CartItemEntity
import com.example.fooddeliveryandroid.data.local.room.dao.CartItemDao
import com.example.fooddeliveryandroid.data.local.room.relation.CartItemWithProduct
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CartLocalDataSource @Inject constructor(
    private val cartItemDao: CartItemDao
) {

    fun observeCartItems(): Flow<List<CartItemWithProduct>> {
        return cartItemDao.observeCartItems()
    }

    suspend fun getCartItem(productId: Long): CartItemEntity? {
        return cartItemDao.getByProductId(productId)
    }

    suspend fun insert(item: CartItemEntity) {
        cartItemDao.insert(item)
    }

    suspend fun insertAll(items: List<CartItemEntity>) {
        cartItemDao.insertAll(items)
    }

    suspend fun updateQuantity(
        productId: Long,
        quantity: Int
    ) {
        cartItemDao.updateQuantity(productId, quantity)
    }

    suspend fun delete(productId: Long) {
        cartItemDao.delete(productId)
    }

    suspend fun clear() {
        cartItemDao.clear()
    }
}