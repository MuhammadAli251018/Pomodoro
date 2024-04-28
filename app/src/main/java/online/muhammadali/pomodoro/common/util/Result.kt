package online.muhammadali.pomodoro.common.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow

sealed interface Result<T>

data class Success<T> (val data: T) : Result<T>

data class Failure<T> (val throwable: Throwable) : Result<T>


inline fun <T> Result<T>.onSuccess(block: T.() -> Unit) = apply {
    if (this is Success)
        block(data)
}

inline fun <T> Result<T>.onFailure(block: Throwable.() -> Unit) = apply {
    if (this is Failure)
        block(throwable)
}

fun <T> Result<T>.getOrNull() =
    if (this is Success)
        data
    else
        null

fun <T> Result<T>.getOrThrow(): T {
    return when(this) {
        is Success -> data
        is Failure -> throw throwable
    }
}

suspend fun <T, C : FlowCollector<T>> Flow<Result<T>>.extractData(flowCollector: C, onFailure: Throwable.() -> Unit) = flowCollector.apply {
    this@extractData.collectLatest { result ->
        result.onSuccess {
            flowCollector.emit(this)
        }
            .onFailure(onFailure)
    }
}

fun <T> Flow<Result<T>>.extractData(onFailure: Throwable.() -> Unit) = flow {
    this@extractData.collectLatest { result ->
        result.onSuccess {
                this@flow.emit(this)
        }.onFailure(onFailure)
    }
}
