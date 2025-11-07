package com.dixitpatel.dchatdemo.feature_chat.data.utils

import android.content.Context
import com.dixitpatel.dchatdemo.R
import com.dixitpatel.dchatdemo.feature_chat.domain.utils.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class SafeApiCall @Inject constructor(@ApplicationContext val context: Context, val coroutineDispatcher: CoroutineDispatcher) {

    suspend inline fun <T> execute(
        crossinline body: suspend () -> T
    ): Resource<T> {
        return try {
            Resource.Success(
                withContext(CoroutineScope(coroutineDispatcher).coroutineContext) { body() }
            )
        } catch (e: Exception) {
            Resource.Error(
                when (e) {
                    is IOException -> context.getString(R.string.error_connection)
                    is HttpException -> {
                         when (e.code()) {
                            404 -> context.getString(R.string.not_found)
                            429 -> context.getString(R.string.usage_limit_reached)
                            500 -> context.getString(R.string.internal_server_error)
                            else -> e.message ?: context.getString(R.string.something_went_wrong)
                        }
                    }
                    else -> context.getString(R.string.error_unknown)
                }
            )
        }
    }
}