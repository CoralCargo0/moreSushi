package by.trokay.more.sushi.common

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat

object Constants {
    const val personalPrefs = "personal_data"
    const val nameKey = "name"
    const val phoneKey = "phone"

    const val ordersPath = "orders"

    @SuppressLint("SimpleDateFormat")
    val dateFormatForAPI: DateFormat = SimpleDateFormat("yyyy.MM.dd - hh.mm.ss")
}