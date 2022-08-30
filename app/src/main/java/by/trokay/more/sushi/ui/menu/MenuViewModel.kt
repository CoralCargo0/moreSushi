package by.trokay.more.sushi.ui.menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.trokay.more.sushi.common.Resource
import by.trokay.more.sushi.data.remote.OrderDto
import by.trokay.more.sushi.domain.OrderSender
import by.trokay.more.sushi.domain.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val repository: ProductsRepository,
    private val sender: OrderSender
) : ViewModel() {

    var selectedTab = 0
    private val _state = MutableStateFlow(MenuState())
    var state: StateFlow<MenuState> = _state
    var productsTypes: HashMap<String, Int> = hashMapOf()

    fun loadProducts() {
        repository.getProducts().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    productsTypes = repository.getProductsTypes()
                    _state.value = MenuState(result.data, isLoading = false)
                }
                is Resource.Loading -> {
                    _state.value = MenuState(isLoading = true)
                }
                else -> {
                    _state.value = MenuState(isLoading = false, error = result.message)
                }
            }
        }.launchIn(CoroutineScope(Dispatchers.IO))

    }

    fun sendOrder(orderDto: OrderDto): Flow<Resource<Boolean>> {
        return sender.sendOrder(orderDto)
    }
}