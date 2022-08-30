package by.trokay.more.sushi.data.datasource

import by.trokay.more.sushi.common.Constants.menuPath
import by.trokay.more.sushi.common.Constants.typesPath
import by.trokay.more.sushi.common.Resource
import by.trokay.more.sushi.domain.datasource.ProductsDataSource
import by.trokay.more.sushi.domain.product.Product
import by.trokay.more.sushi.data.remote.ProductTypeDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class ProductsDataSourceImpl @Inject constructor() : ProductsDataSource {

    var db = FirebaseFirestore.getInstance()
    var productsList = mutableListOf<Product>()
    val foodTypes: HashMap<String, Int> = hashMapOf()

    private val _products =
        MutableStateFlow<Resource<List<Product>>>(Resource.Loading<List<Product>>())
    var products: StateFlow<Resource<List<Product>>> = _products

    override fun getProducts(): Flow<Resource<List<Product>>> {
        if (foodTypes.isEmpty()) {
            _products.value = Resource.Loading<List<Product>>()
            db.collection(typesPath).get()
                .addOnSuccessListener { querySnapshotTypes ->
                    querySnapshotTypes.documents.forEach { v ->
                        v.toObject(ProductTypeDto::class.java)?.let { foodTypes[it.title] = it.index }
                    }

                    db.collection(menuPath).get()
                        .addOnSuccessListener { querySnapshot ->
                            querySnapshot.documents.forEach { v ->
                                v.toObject(Product::class.java)?.let { productsList.add(it) }
                            }
                            productsList.sortBy { it.type }
                            _products.value =
                                Resource.Success<List<Product>>(productsList)

                        }
                        .addOnFailureListener { e ->
                            _products.value =
                                Resource.Error<List<Product>>(
                                    e.localizedMessage ?: "An unexpected error occured"
                                )
                        }

                }
                .addOnFailureListener { e ->
                    _products.value =
                        Resource.Error<List<Product>>(
                            e.localizedMessage ?: "An unexpected error occured"
                        )
                }
        }
        return products
    }

    override fun getTypes(): HashMap<String, Int> {
        return foodTypes
    }

    override fun getProduct(id: Int): Product? {
        productsList.find { it.id == id }?.let { return it }
        return null
    }
}