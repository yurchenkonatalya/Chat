package com.example.chat.network.responses

data class UserListResponse(
    var users: List<AuthorizeUserResponse>?,
    var status_code: Int?
)