package ru.alexp0111.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

private const val TAG = "CoroutineViewModel error"

abstract class CoroutineViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        viewModelScope.coroutineContext + CoroutineExceptionHandler(::errorHandler)
    }

    open fun errorHandler(context: CoroutineContext, throwable: Throwable) {
        Log.d(TAG, throwable.toString())
    }
}