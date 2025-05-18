package ru.alexp0111.core.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import retrofit2.HttpException
import ru.alexp0111.core.model.error.ApiErrorModel
import kotlin.coroutines.CoroutineContext

private const val TAG = "CoroutineViewModel error"
private const val TAG_HTTP = "CoroutineViewModel http-error"

abstract class CoroutineViewModel : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext by lazy {
        viewModelScope.coroutineContext + CoroutineExceptionHandler { context, throwable ->
            errorHandler(context, throwable)

            tryGetErrorMessage(throwable)?.let { message ->
                tryGetErrorCode(throwable)?.let { code ->
                    httpErrorHandler(message, code)
                }
            }
        }
    }

    open fun errorHandler(context: CoroutineContext, throwable: Throwable) {
        Log.d(TAG, throwable.toString())
    }

    open fun httpErrorHandler(errorMessage: String, errorCode: Int) {
        Log.d("$TAG_HTTP [$errorCode]", errorMessage)
    }

    private fun tryGetErrorCode(throwable: Throwable): Int? =
        (throwable as? HttpException)?.response()?.code()

    private fun tryGetErrorMessage(throwable: Throwable): String? =
        (throwable as? HttpException)?.response()?.errorBody()?.string()
            ?.let { Gson().fromJson(it, ApiErrorModel::class.java) }
            ?.error
}