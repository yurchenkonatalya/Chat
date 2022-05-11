package com.example.chat.network.api.user


import com.example.chat.network.responses.AuthorizeUserResponse
import com.example.chat.network.responses.StatusCodeResponse
import com.google.android.gms.common.api.Response
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserApi {
    @FormUrlEncoded
    @POST("user/registerUser.php")
    suspend fun registerUser(
        @Field("login") login: String,
        @Field("password") password: String,
        @Field("name") name: String,
        @Field("surname") surname: String,
        @Field("phone") phone: String,
        @Field("mail") mail: String,
        @Field("googleId") googleId: String,
        @Field("birthday") birthday: String,
        @Field("photo") photo: String
    ): retrofit2.Response<StatusCodeResponse>

    @FormUrlEncoded
    @POST("user/authorizeUser.php")
    suspend fun authorizeUser(
        @Field("login") login: String,
        @Field("password") password: String,
    ): retrofit2.Response<AuthorizeUserResponse>


}