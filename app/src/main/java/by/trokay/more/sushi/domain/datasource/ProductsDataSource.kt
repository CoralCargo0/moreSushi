package by.trokay.more.sushi.domain.datasource

import by.trokay.more.sushi.common.Resource
import by.trokay.more.sushi.domain.product.Product
import kotlinx.coroutines.flow.Flow

interface ProductsDataSource {
    fun getProducts(): Flow<Resource<List<Product>>>
    fun getTypes(): HashMap<String, Int>
}