package com.example.chat.network.responses

data class MessageListResponse(
    var status_code: Int?,
    var id: Long?,
    var date: String?,
    var id_user_source: Long?,
    var id_user_destination: Long?,
    var message: String?,
    var info: List<String>?,
    var is_read: Int?
)