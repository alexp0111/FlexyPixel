package ru.alexp0111.bt_service.model.transfer

sealed interface TransferResult {
    data class TransferSucceeded(val message: String): TransferResult
    data class Error(val errorMessage: String): TransferResult
}