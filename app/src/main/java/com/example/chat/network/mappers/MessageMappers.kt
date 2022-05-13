package com.example.chat.network.mappers

import com.example.chat.DB.entity.DialogEntity
import com.example.chat.DB.entity.MessageEntity
import com.example.chat.network.responses.DialogListResponse
import com.example.chat.network.responses.MessageListResponse

fun DialogListResponse.toDialogEntity(
    id_authorized_user: Long,
    responseIndex: Int = -1
): DialogEntity? {
    val messageInfo = if (message_info?.size ?: 0 > 0) message_info?.get(0) else null
    return DialogEntity(
        message_id ?: return null,
        message_date ?: return null,
        message_text ?: return null,
        messageInfo,
        message_is_read ?: return null,
        if (id_authorized_user == sender_id) {
            receiver_id ?: return null
        } else {
            sender_id ?: return null
        },
        if (id_authorized_user == sender_id) {
            receiver_name ?: return null
        } else {
            sender_name ?: return null
        },
        if (id_authorized_user == sender_id) {
            receiver_surname ?: return null
        } else {
            sender_surname ?: return null
        },
        if (id_authorized_user == sender_id) {
            receiver_nickname ?: return null
        } else {
            sender_nickname ?: return null
        },
        if (id_authorized_user == sender_id) {
            receiver_photo ?: return null
        } else {
            sender_photo ?: return null
        },
        if (id_authorized_user == sender_id) {
            receiver_last_active ?: return null
        } else {
            sender_last_active ?: return null
        },
        responseIndex
    )
}

fun MessageListResponse.toMessageEntity(): MessageEntity? {
    val messageInfo = if (info?.size ?: 0 > 0) info?.get(0) else null
    return MessageEntity(
        id ?: return null,
        date ?: return null,
        id_user_source ?: return null,
        id_user_destination ?: return null,
        message ?: return null,
        messageInfo,
        is_read ?: return null
    )
}