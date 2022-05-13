package com.example.chat.network.responses

data class DialogListResponse(
    var status_code: Int?,
    var message_id: Long?,
    var message_date: String?,
    var message_text: String?,
    var message_info: List<String>?,
    var message_is_read: Int?,

    var sender_id: Long?,
    var sender_name: String?,
    var sender_surname: String?,
    var sender_nickname: String?,
    var sender_photo: String?,
    var sender_last_active: String?,

    var receiver_id: Long?,
    var receiver_name: String?,
    var receiver_surname: String?,
    var receiver_nickname: String?,
    var receiver_photo: String?,
    var receiver_last_active: String?
)