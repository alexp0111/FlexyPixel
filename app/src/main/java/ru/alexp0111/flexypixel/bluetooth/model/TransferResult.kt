package ru.alexp0111.flexypixel.bluetooth.model

sealed interface TransferResult {
    data class TransferSucceeded(val message: String): TransferResult
    data class Error(val errorMessage: String): TransferResult
}