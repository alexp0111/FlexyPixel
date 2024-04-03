package ru.alexp0111.flexypixel.bluetooth.utils

object MessageConverter {
    fun convert(
        num: Int,
        totalAmountOfSigns: Int = 3,
    ): String {
        val stringNumber = num.toString()
        return when {
            stringNumber.length == totalAmountOfSigns -> stringNumber
            stringNumber.length > totalAmountOfSigns -> {
                throw IllegalStateException("Integer value is too big")
            }

            else -> {
                "0".repeat(totalAmountOfSigns - stringNumber.length) + stringNumber
            }
        }
    }

}