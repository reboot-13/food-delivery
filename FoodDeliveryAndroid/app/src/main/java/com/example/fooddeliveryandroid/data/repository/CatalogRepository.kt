package com.example.fooddeliveryandroid.data.repository

import com.example.fooddeliveryandroid.data.local.datastore.UserSession
import com.example.fooddeliveryandroid.data.remote.dto.request.AddCartItemRequest
import com.example.fooddeliveryandroid.data.remote.network.NetworkResult
import com.example.fooddeliveryandroid.domain.model.CatalogData
import com.example.fooddeliveryandroid.domain.model.CatalogProduct
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CatalogRepository @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val cartRepository: CartRepository,
    private val userSession: UserSession
) {

    fun observeCatalogData(): Flow<CatalogData> {
        return userSession.currentUser.flatMapLatest { user ->
            if (user == null) {
                combine(
                    productRepository.observeProducts(),
                    categoryRepository.observeCategories()
                ) {products, categories ->
                    CatalogData(
                        categories = categories,
                        products = products.map {
                            CatalogProduct(
                                product = it,
                                quantity = 0
                            )
                        }
                    )
                }
            } else {
                combine(
                    productRepository.observeProducts(),
                    categoryRepository.observeCategories(),
                    cartRepository.observeCartItems()
                ) { products, categories, cartItems ->
                    val quantities = cartItems.associate { cartItem ->
                        cartItem.product.id to cartItem.quantity
                    }

                    CatalogData (
                        categories = categories,
                        products = products.map { product ->
                            CatalogProduct (
                                product = product,
                                quantity = quantities[product.id] ?: 0
                            )
                        }
                    )
                }
            }


        }
    }

    suspend fun syncCatalog(): NetworkResult<Unit> {
        return coroutineScope {
            val productsDeferred = async {
                productRepository.syncProducts()
            }

            val categoryDeferred = async {
                categoryRepository.syncCategories()
            }

            val products = productsDeferred.await()
            val categories = categoryDeferred.await()

            when {
                products is NetworkResult.Error ->
                    products

                categories is NetworkResult.Error ->
                    categories

                else ->
                    NetworkResult.Success(Unit)
            }
        }
    }
}