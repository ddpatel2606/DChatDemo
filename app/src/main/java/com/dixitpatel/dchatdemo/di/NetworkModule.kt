package com.dixitpatel.dchatdemo.di

import android.content.Context
import com.appmattus.certificatetransparency.BasicAndroidCTLogger
import com.appmattus.certificatetransparency.CTInterceptorBuilder
import com.dixitpatel.dchatdemo.BuildConfig
import com.dixitpatel.dchatdemo.feature_chat.data.consts.CACHE_SIZE
import com.dixitpatel.dchatdemo.feature_chat.data.consts.HTTP_REQUEST_TIMEOUT
import com.dixitpatel.dchatdemo.feature_chat.data.consts.SECURED_OKHTTP_CLIENT
import com.dixitpatel.dchatdemo.feature_chat.data.consts.UNSECURED_OKHTTP_CLIENT
import com.dixitpatel.dchatdemo.feature_chat.data.remote.api_interface.ChatInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


/**
 * Dagger Hilt module that provides network-related dependencies for the application.
 *
 * This module is responsible for setting up and configuring networking components like
 * OkHttpClient, Retrofit, and the API service interface. It provides different
 * configurations for debug and release builds, such as enabling logging and certificate
 * transparency.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        return interceptor
    }

    @Singleton
    @Provides
    @Named(UNSECURED_OKHTTP_CLIENT)
    fun provideUnSecureOkHttpClient(
        @ApplicationContext context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().cache(Cache(context.cacheDir, CACHE_SIZE))
            .addInterceptor(httpLoggingInterceptor)
            .connectTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS).build()
    }

    @Singleton
    @Provides
    @Named(SECURED_OKHTTP_CLIENT)
    fun provideSecuredOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val interceptor = CTInterceptorBuilder().setLogger(BasicAndroidCTLogger(false)).build()
        return OkHttpClient.Builder().cache(Cache(context.cacheDir, CACHE_SIZE))
            .addNetworkInterceptor(interceptor)
            .connectTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(HTTP_REQUEST_TIMEOUT, TimeUnit.SECONDS).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        @Named(SECURED_OKHTTP_CLIENT) securedOkHttpClient: OkHttpClient,
        @Named(UNSECURED_OKHTTP_CLIENT) unSecuredOkHttpClient: OkHttpClient,
    ): Retrofit = Retrofit.Builder()
        .client(if (BuildConfig.DEBUG) unSecuredOkHttpClient else securedOkHttpClient)
        .baseUrl(BuildConfig.CHAT_BASE_URL).addConverterFactory(GsonConverterFactory.create())
        .build()

    @Singleton
    @Provides
    fun provideChatInterface(retrofit: Retrofit): ChatInterface = retrofit.create(ChatInterface::class.java)

}