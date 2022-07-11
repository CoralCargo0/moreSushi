package by.trokay.more.sushi.data

import by.trokay.more.sushi.common.Resource
import by.trokay.more.sushi.data.remote.OrderDto
import by.trokay.more.sushi.domain.OrderSender
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class OrderSenderImpl @Inject constructor() : OrderSender {

    private val ordersPath = "orders"
    var db = FirebaseFirestore.getInstance()
    private val _state =
        MutableStateFlow<Resource<Boolean>>(Resource.Loading<Boolean>())
    var state: StateFlow<Resource<Boolean>> = _state

    override fun sendOrder(order: OrderDto): Flow<Resource<Boolean>> {
        db.collection(ordersPath).document(order._6time).set(order)
            .addOnSuccessListener {
                _state.value = Resource.Success(true)
            }
            .addOnFailureListener {
                _state.value = Resource.Error(it.message ?: "Unknown error")
            }
        return state
    }
}