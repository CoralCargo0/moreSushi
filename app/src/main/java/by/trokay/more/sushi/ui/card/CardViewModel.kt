package by.trokay.more.sushi.ui.card

import androidx.lifecycle.ViewModel
import by.trokay.more.sushi.domain.repository.ProductsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CardViewModel  @Inject constructor(
    private val repository: ProductsRepository
) : ViewModel() {
    fun getProduct(id: Int) = repository.getProduct(id)
}