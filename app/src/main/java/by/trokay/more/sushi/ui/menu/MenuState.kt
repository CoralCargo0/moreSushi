package by.trokay.more.sushi.ui.menu

import by.trokay.more.sushi.domain.product.Product

data class MenuState(
    val menu: List<Product>? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)