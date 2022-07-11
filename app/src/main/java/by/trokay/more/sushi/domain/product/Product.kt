package by.trokay.more.sushi.domain.product

data class Product(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val constitution: List<String> = listOf(),
    val price: Double = 0.0,
    val image: String = "",
    val type: Int = 0,
    var amount: Int = 0
)