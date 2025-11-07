package com.dixitpatel.dchatdemo.feature_chat.domain.models

data class User(
    val id: String,
    val name: String,
    val profilePic: Int = 0
)
