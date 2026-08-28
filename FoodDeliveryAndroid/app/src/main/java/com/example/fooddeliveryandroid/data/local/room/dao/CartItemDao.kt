package com.example.fooddeliveryandroid.data.local.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fooddeliveryandroid.data.local.entity.CartItemEntity
import com.example.fooddeliveryandroid.data.local.room.relation.CartItemWithProduct
import com.example.fooddeliveryandroid.domain.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Query (
        """
        SELECT
            ci.productId AS productId,
            ci.quantity AS quantity,

            p.name AS productName,
            p.price AS price,
            p.imageUrl AS imageUrl,
            p.weight AS weight,
            p.available AS available,

            c.id AS categoryId,
            c.name AS categoryName

        FROM cart_items ci

        INNER JOIN products p
            ON ci.productId = p.id

        INNER JOIN categories c
            ON p.categoryId = c.id
        """
    )
    fun observeCartItems(): Flow<List<CartItemWithProduct>>

    @Query(
        "SELECT quantity FROM cart_items WHERE productId = :productId"
    )
    fun observeQuantity(productId: Long): Flow<Int>

    @Query("""
        SELECT * 
        FROM cart_items
        WHERE productId = :productId
    """)
    suspend fun getByProductId(
        productId: Long
    ): CartItemEntity?

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItemEntity)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CartItemEntity>)

    @Query (
        "UPDATE cart_items SET quantity = :quantity WHERE productId = :productId"
    )
    suspend fun updateQuantity(productId: Long, quantity: Int)

    @Query("DELETE FROM cart_items WHERE productId = :productId")
    suspend fun delete(productId: Long)

    @Query ("DELETE FROM cart_items")
    suspend fun clear()
}