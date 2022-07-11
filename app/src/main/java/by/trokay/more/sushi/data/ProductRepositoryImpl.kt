package by.trokay.more.sushi.data

import by.trokay.more.sushi.common.Resource
import by.trokay.more.sushi.domain.datasource.ProductsDataSource
import by.trokay.more.sushi.domain.product.Product
import by.trokay.more.sushi.domain.repository.ProductsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val dataSource: ProductsDataSource
) : ProductsRepository {

    override fun getProducts(): Flow<Resource<List<Product>>> {
        return dataSource.getProducts()
    }

    override fun getProductsTypes(): HashMap<String, Int> {
        return dataSource.getTypes()
    }
}