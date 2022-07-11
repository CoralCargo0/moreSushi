package by.trokay.more.sushi.domain

import by.trokay.more.sushi.common.Resource
import by.trokay.more.sushi.data.remote.OrderDto
import kotlinx.coroutines.flow.Flow

interface OrderSender {
    fun sendOrder(order: OrderDto): Flow<Resource<Boolean>>
}