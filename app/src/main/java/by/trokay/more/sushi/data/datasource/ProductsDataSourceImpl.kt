package by.trokay.more.sushi.data.datasource

import by.trokay.more.sushi.common.Resource
import by.trokay.more.sushi.domain.datasource.ProductsDataSource
import by.trokay.more.sushi.domain.product.Product
import by.trokay.more.sushi.data.remote.ProductTypeDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class ProductsDataSourceImpl @Inject constructor() : ProductsDataSource {

    val menuPath = "menu"
    val typesPath = "types"

    var db = FirebaseFirestore.getInstance()
//
//    val testList1 = mutableListOf(
//        Product(
//            1,
//            "Двойной Биг Мак™",
//            "Классический Биг Мак, заправленный пикантным соусом Биг Мак, свежим салатом, луком, маринованными огурчиками, и нежным сыром Чеддер, с четырьмя большими сочными говяжьими котлетами.",
//            listOf("2809 кДж", "672 ккал", "34"),
//            10.30,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%B1%D1%83%D1%80%D0%B3%D0%B5%D1%80%D1%8B%20%D0%B8%20%D1%80%D0%BE%D0%BB%D0%BB%D1%8B/%D0%A1%D0%B0%D0%B8%CC%86%D1%82-SOK-%D0%94%D0%B2%D0%BE%D0%B8%CC%86%D0%BD%D0%BE%D0%B8%CC%86-%D0%91%D0%B8%D0%B3-%D0%9C%D0%B0%D0%BA.png"),
//            2
//        ),
//        Product(
//            2,
//            "Двойной Роял",
//            "Смотри, сколько мяса! Бургер с двумя большими сочными бифштексами из 100% говядины, двумя ломтиками сыра Чеддер, маринованными огурчиками, луком, горчицей и кетчупом. И все это — в ароматной булочке c кунжутом.",
//            listOf("3804 кДж", "911 ккал", "46"),
//            10.5,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%B1%D1%83%D1%80%D0%B3%D0%B5%D1%80%D1%8B%20%D0%B8%20%D1%80%D0%BE%D0%BB%D0%BB%D1%8B/%D0%A1%D0%B0%D0%B8%CC%86%D1%82-SOK-%D0%94%D0%B2%D0%BE%D0%B8%CC%86%D0%BD%D0%BE%D0%B8%CC%86-%D0%A0%D0%BE%D1%8F%D0%BB.png"),
//            2
//        ),
//        Product(
//            3,
//            "МакЧикен™ Премьер",
//            "Три рубленых бифштекса из натуральной цельной говядины с тремя кусочками сыра Чеддер на булочке, заправленной горчицей, кетчупом, луком и двумя кусочками маринованного огурчика.",
//            listOf("2216 кдж", "530 ккал", "27"),
//            7.6,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%B1%D1%83%D1%80%D0%B3%D0%B5%D1%80%D1%8B%20%D0%B8%20%D1%80%D0%BE%D0%BB%D0%BB%D1%8B/%D0%A7%D0%B8%D0%BA%D0%B5%D0%BD-%D0%BF%D1%80%D0%B5%D0%BC%D1%8C%D0%B5%D1%80.png"),
//            2
//        ),
//        Product(
//            4,
//            "Тройной чизбургер",
//            "Три рубленых бифштекса из натуральной цельной говядины с тремя кусочками сыра Чеддер на булочке, заправленной горчицей, кетчупом, луком и двумя кусочками маринованного огурчика.",
//            listOf("2496 кДж", "598 ккал", "30"),
//            7.9,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%B1%D1%83%D1%80%D0%B3%D0%B5%D1%80%D1%8B%20%D0%B8%20%D1%80%D0%BE%D0%BB%D0%BB%D1%8B/_%D0%A2%D1%80%D0%BE%D0%B9%D0%BD%D0%BE%D0%B9%D0%A7%D0%B8%D0%B7%D0%B1%D1%83%D1%80%D0%B3%D0%B5%D1%80.png"),
//            2
//        ),
//        Product(
//            5,
//            "Биг Мак™",
//            "Большой сандвич с двумя рубленными бифштексами из натуральной цельной говядины на специальной булочке, заправленной луком,  двумя кусочками маринованного огурчика, ломтиком сыра чеддер, свежим салатом и специальным соусом.",
//            listOf("2131 кДж", "509 ккал", "25"),
//            6.9,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%B1%D1%83%D1%80%D0%B3%D0%B5%D1%80%D1%8B%20%D0%B8%20%D1%80%D0%BE%D0%BB%D0%BB%D1%8B/%D0%91%D0%B8%D0%B3-%D0%9C%D0%B0%D0%BA.png"),
//            2
//        ),
//        Product(
//            6,
//            "Картофель по-деревенски",
//            "Вкусные, обжаренные в растительном фритюре ломтики картофеля со специями.",
//            listOf("1380 кДж", "330 ккал", "16"),
//            4.5,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%BA%D0%B0%D1%80%D1%82%D0%BE%D1%84%D0%B5%D0%BB%D1%8C%20%D0%B8%20%D1%81%D1%82%D0%B0%D1%80%D1%82%D0%B5%D1%80%D1%8B/%D0%9A%D0%B0%D1%80%D1%82%D0%BE%D1%84%D0%B5%D0%BB%D1%8C-%D0%BF%D0%BE-%D0%B4%D0%B5%D1%80%D0%B2%D0%B5%D0%BD%D1%81%D0%BA%D0%B8-M.png"),
//            4
//        ),
//        Product(
//            7,
//            "Картофель фри",
//            "Хрустящие, золотистые, обжаренные в растительном фритюре и слегка посоленные соломки отборного картофеля",
//            listOf("1000 кдж", "239 ккал", "12"),
//            4.4,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%BA%D0%B0%D1%80%D1%82%D0%BE%D1%84%D0%B5%D0%BB%D1%8C%20%D0%B8%20%D1%81%D1%82%D0%B0%D1%80%D1%82%D0%B5%D1%80%D1%8B/%D0%9A%D0%B0%D1%80%D1%82%D0%BE%D1%84%D0%B5%D0%BB%D1%8C-%D1%84%D1%80%D0%B8-S.png"),
//            4
//        ),
//        Product(
//            8,
//            "Вафельный рожок",
//            "Нежное мороженое из натурального цельного молока в хрустящем вафельном рожке.",
//            listOf("549 кДж", "130 ккал", "7"),
//            1.9,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%B4%D0%B5%D1%81%D0%B5%D1%80%D1%82%D1%8B/%D0%A1%D0%B0%D0%B8%CC%86%D1%82-SOK%20%D0%9D%D0%BE%D0%B2%D1%8B%D0%B8%CC%86-%D1%80%D0%BE%D0%B6%D0%BE%D0%BA.png"),
//            5
//        ),
//        Product(
//            9,
//            "Молочный коктейль шоколадный",
//            "Ароматный и великолепно взбитый коктейль, приготовленный из высококачественной молочной смеси и шоколадного сиропа.",
//            listOf("1049 кДж", "249 ккал", "12"),
//            4.5,
//            listOf("https://mcdonalds.by/images/product-images/Desserts/Menuby-choco-shake.jpg"),
//            6
//        ),
//        Product(
//            10,
//            "Молочный коктейль клубничный",
//            "Ароматный и великолепно взбитый коктейль, приготовленный из высококачественной молочной смеси и клубничного сиропа.",
//            listOf("1027 кДж", "243 ккал", "12"),
//            4.5,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%B4%D0%B5%D1%81%D0%B5%D1%80%D1%82%D1%8B/Menuby-srobarry-shake.jpg"),
//            6
//        ),
//        Product(
//            11,
//            "Bonaqua®",
//            "Питевая вода Bonaqua объёмом 500 мл.",
//            listOf("0 кДж", "0 ккал", "0"),
//            1.9,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%BD%D0%B0%D0%BF%D0%B8%D1%82%D0%BA%D0%B8/%D0%91%D0%B0%D0%BD%D0%B0%D0%BA%D0%B2%D0%B0-%D0%BD%D0%B5%D0%B3%D0%B0%D0%B7%D0%B8%D1%80%D0%BE%D0%B2%D0%B0%D0%BD%D0%BD%D0%B0%D1%8F.png"),
//            6
//        ),
//        Product(
//            12,
//            "Апельсиновый сок",
//            "Свежий освежающий апельсиновый сок, приготовленный из натурального концентрата.",
//            listOf("647 кДж", "152 ккал", "8"),
//            3.8,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%BD%D0%B0%D0%BF%D0%B8%D1%82%D0%BA%D0%B8/%D0%90%D0%BF%D0%B5%D0%BB%D1%8C%D1%81%D0%B8%D0%BD%D0%BE%D0%B2%D1%8B%D0%B9-%D1%81%D0%BE%D0%BA.png"),
//            6
//        ),
//        Product(
//            13,
//            "Чай Черный",
//            "Ароматный согревающий черный чай Сurtis в пирамидках в индивидуальной упаковке.",
//            listOf("0 кДж", "0 ккал", "0"),
//            2.6,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%B3%D0%BE%D1%80%D1%8F%D1%87%D0%B8%D0%B5%20%D0%BD%D0%B0%D0%BF%D0%B8%D1%82%D0%BA%D0%B8/%D0%A7%D0%B5%D1%80%D0%BD%D1%8B%D0%B9-%D1%87%D0%B0%D0%B9.png"),
//            7
//        ),
//        Product(
//            14,
//            "Чай Зеленый",
//            "Ароматный согревающий зелёный чай Сurtis в пирамидках в индивидуальной упаковке.",
//            listOf("0 кДж", "0 ккал", "0"),
//            2.6,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%B3%D0%BE%D1%80%D1%8F%D1%87%D0%B8%D0%B5%20%D0%BD%D0%B0%D0%BF%D0%B8%D1%82%D0%BA%D0%B8/%D0%97%D0%B5%D0%BB%D0%BD%D1%8B%D0%B9-%D1%87%D0%B0%D0%B9.png"),
//            7
//        ),
//        Product(
//            15,
//            "Соус Сырный",
//            "",
//            listOf("376 кДж", "91 ккал", "0"),
//            1.0,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D1%81%D0%BE%D1%83%D1%81%D1%8B/%D0%A1%D1%8B%D1%80%D0%BD%D1%8B%D0%B9.png"),
//            8
//        ),
//        Product(
//            16,
//            "Кетчуп",
//            "",
//            listOf("148 кДж", "35 ккал", "0"),
//            1.0,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D1%81%D0%BE%D1%83%D1%81%D1%8B/%D0%9A%D0%B5%D1%82%D1%87%D1%83%D0%BF.png"),
//            8
//        ),
//        Product(
//            17,
//            "Хэппи Мил™ с чикенбургером и напитком на выбор",
//            "Детский обеденный комплекс с игрушкой «Хэппи Мил» с составляющими на выбор.",
//            listOf("0 кДж", "540 ккал", "27"),
//            8.5,
//            listOf("https://mcdonalds.by/images/product-images/omnichannel/%D0%A5%D0%9C/385020482-_5.jpg"),
//            9
//        )
//
//    )

//    val typesTest: HashMap<String, Int> = hashMapOf(
//        Pair("Всё", 0),
//        Pair("Боксы", 1),
//        Pair("Бургеры", 2),
//        Pair("Роллы", 3),
//        Pair("Картофель", 4),
//        Pair("Десерты", 5),
//        Pair("Напитки", 6),
//        Pair("Кофе и чай", 7),
//        Pair("Соусы", 8),
//        Pair("Хэппи Мил™", 9),
//    )

    val typesTest = listOf(
        ProductTypeDto("Всё", 0),
        ProductTypeDto("Боксы", 1),
        ProductTypeDto("Бургеры", 2),
        ProductTypeDto("Роллы", 3),
        ProductTypeDto("Картофель", 4),
        ProductTypeDto("Десерты", 5),
        ProductTypeDto("Напитки", 6),
        ProductTypeDto("Кофе и чай", 7),
        ProductTypeDto("Соусы", 8),
        ProductTypeDto("Хэппи Мил™", 9)
    )


    var productsList = mutableListOf<Product>()
    val typess: HashMap<String, Int> = hashMapOf()

    private val _products =
        MutableStateFlow<Resource<List<Product>>>(Resource.Loading<List<Product>>())
    var products: StateFlow<Resource<List<Product>>> = _products

    override fun getProducts(): Flow<Resource<List<Product>>> {
//        testList.forEach{
//            db.collection(menuPath).document(it.title).set(it)
//        }
//        typesTest.forEach {
//            db.collection(typesPath).document(it.title).set(it)
//        }
        if (typess.isEmpty()) {
            _products.value = Resource.Loading<List<Product>>()
            db.collection(typesPath).get()
                .addOnSuccessListener { querySnapshotTypes ->
                    querySnapshotTypes.documents.forEach { v ->
                        v.toObject(ProductTypeDto::class.java)?.let { typess[it.title] = it.index }
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
        return typess
    }

    override fun getProduct(id: Int): Product? {
        productsList.find { it.id == id }?.let { return it }
        return null
    }
}