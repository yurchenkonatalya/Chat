package com.example.chat

import android.util.Log
import retrofit2.Response

object QueryExecutor {
    suspend fun <T : Any> getResponse(
        queryFunc: suspend () -> Response<T>
    ): T? {
        lateinit var response: Response<T>
        try {
            response = queryFunc()
        } catch (e: Exception) {
            Log.e("RESSS", e.toString())
            return null
        }
        if (!response.isSuccessful)
            return null
        return response.body()
    }
}