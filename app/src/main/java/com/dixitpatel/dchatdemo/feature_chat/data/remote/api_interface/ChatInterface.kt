package com.dixitpatel.dchatdemo.feature_chat.data.remote.api_interface

import retrofit2.http.GET

/**
 * Retrofit API interface for chat-related network operations.
 * Defines the endpoints for fetching chat and user data from the remote server.
 */
interface ChatInterface {

    @GET("chatList")
    suspend fun getChatList(): List<String>

    @GET("userList")
    suspend fun getUserList(): List<String>
}