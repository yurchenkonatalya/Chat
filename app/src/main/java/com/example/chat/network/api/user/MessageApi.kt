package com.example.chat.network.api.user

import com.example.chat.network.responses.DialogListResponse
import com.example.chat.network.responses.MessageListResponse
import com.example.chat.network.responses.StatusCodeResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MessageApi {
    @FormUrlEncoded
    @POST("message/getDialogList.php")
    suspend fun getDialogList(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("page") page: Int,
        @Field("search") search: String?
    ): retrofit2.Response<List<DialogListResponse>>

    @FormUrlEncoded
    @POST("message/getLastDialogList.php")
    suspend fun getLastDialogList(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("id_start") id_start: Long,
        @Field("search") search: String?
    ): retrofit2.Response<List<DialogListResponse>>

    @FormUrlEncoded
    @POST("message/getMessageList.php")
    suspend fun getMessageList(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("page") page: Int,
        @Field("id_user") id_user: Long
    ): retrofit2.Response<List<MessageListResponse>>

    @FormUrlEncoded
    @POST("message/getLastMessageList.php")
    suspend fun getLastMessageList(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("id_start") id_start: Long,
        @Field("id_user") id_user: Long
    ): retrofit2.Response<List<MessageListResponse>>

    @FormUrlEncoded
    @POST("message/sendMessage.php")
    suspend fun sendMessage(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("id_user") id_user: Long,
        @Field("text") text: String
    ): retrofit2.Response<StatusCodeResponse>

    @FormUrlEncoded
    @POST("message/sendMessagePhoto.php")
    suspend fun sendMessagePhoto(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("id_user") id_user: Long,
        @Field("photo") photo: String
    ): retrofit2.Response<StatusCodeResponse>
}