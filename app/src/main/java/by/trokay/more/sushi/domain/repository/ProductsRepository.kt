package by.trokay.more.sushi.domain.repository

import by.trokay.more.sushi.common.Resource
import by.trokay.more.sushi.domain.product.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {
    fun getProducts(): Flow<Resource<List<Product>>>
    fun getProductsTypes(): HashMap<String, Int>
    fun getProduct(id: Int): Product?
}