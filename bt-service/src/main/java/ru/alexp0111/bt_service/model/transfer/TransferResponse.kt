package ru.alexp0111.bt_service.model.transfer

data class TransferResponse(
    val status: String,
) {
    companion object {
        const val OK = "OK"
        const val UNCONFIGURED = "UNCONFIGURED"
        const val ERROR = "ERROR"
    }
}