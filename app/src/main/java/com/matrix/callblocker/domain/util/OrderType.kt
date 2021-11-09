package com.matrix.callblocker.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}
