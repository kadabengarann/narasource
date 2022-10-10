package id.co.mka.naraq.core.data.source.remote.network

import id.co.mka.naraq.core.data.source.remote.response.AuthResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("auth/login")
    suspend fun login(
        @Body param: HashMap<String, String>
    ): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(
        @Body param: HashMap<String, String>
    ): Response<AuthResponse>
}
