package ru.alexp0111.flexypixel.bluetooth.utils

object MessageConverter {
    fun convert(
        pixelOrder: Int,
        length: Int = 3,
    ): String {
        val stringNumber = pixelOrder.toString()
        return when {
            stringNumber.length == length -> stringNumber
            stringNumber.length > length -> {
                throw IllegalStateException("Integer value is too big")
            }

            else -> {
                "0".repeat(length - stringNumber.length) + stringNumber
            }
        }
    }

}