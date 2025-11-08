package com.dixitpatel.dchatdemo.feature_chat.domain.utils

/**
 * A generic sealed class that contains data and status about loading this data.
 * It's a common pattern to wrap network responses or any asynchronous operation results.
 *
 * @param T The type of the data held by the resource.
 */
sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}