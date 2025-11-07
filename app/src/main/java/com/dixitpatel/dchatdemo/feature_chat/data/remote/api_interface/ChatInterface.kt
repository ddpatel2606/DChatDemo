package com.dixitpatel.dchatdemo.feature_chat.data.remote.api_interface

import retrofit2.http.GET

interface ChatInterface {

    @GET("chatList")
    suspend fun getChatList(): List<String>

    @GET("userList")
    suspend fun getUserList(): List<String>
}