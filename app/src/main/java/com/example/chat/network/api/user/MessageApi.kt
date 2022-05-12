package com.example.chat.network.api.user

import com.example.chat.network.responses.DialogListResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MessageApi {
    @FormUrlEncoded
    @POST("message/getDialogList.php")
    suspend fun getUserList(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("page") page: Int,
        @Field("search") search: String?
    ): retrofit2.Response<List<DialogListResponse>>
}