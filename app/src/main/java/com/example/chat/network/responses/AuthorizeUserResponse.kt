package com.example.chat.network.responses

data class AuthorizeUserResponse(
    var status_code: Int?,
    var id: Long?,
    var name: String?,
    var surname: String?,
    var mail: String?,
    var phone: String?,
    var nickname: String?,
    var googleId: String?,
    var birthday: String?,
    var lastActive: String?,
    var photo: String?
)