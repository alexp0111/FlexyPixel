package ru.alexp0111.flexypixel.bluetooth.model

data class TransferResponse(
    val status: String,
) {
    companion object {
        const val OK = "OK"
        const val UNCONFIGURED = "UNCONFIGURED"
        const val ERROR = "ERROR"
    }
}