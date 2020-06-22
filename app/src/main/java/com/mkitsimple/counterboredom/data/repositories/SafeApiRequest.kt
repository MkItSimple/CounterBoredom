package com.mkitsimple.counterboredom.data.repositories

import retrofit2.Response
import java.io.IOException

abstract class SafeApiRequest {

    suspend fun<T: Any> apiRequest(call: suspend () -> Response<T>) : T{
        val response = call.invoke()    // response is the suspending function that will give us a response of type <T>
        if(response.isSuccessful){      // Movie.kt
            return response.body()!!
        }else{
            //@todo handle api exception
            throw ApiException(
                response.code().toString()
            )
        }
    }
}

class ApiException(message: String): IOException(message)