package com.matrix.callblocker.utils

class Utils {
    fun convertNumber(number: String) = number.replace(Regex("((?<!^)[^0-9]|^[^0-9+])"), "")
}