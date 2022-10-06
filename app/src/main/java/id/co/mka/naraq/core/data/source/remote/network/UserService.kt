package id.co.mka.naraq.core.data.source.remote.network

import id.co.mka.naraq.core.data.source.remote.request.LoginRequest
import id.co.mka.naraq.core.data.source.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("auth/login")
    suspend fun login(
        @Body body: LoginRequest
    ): Response<LoginResponse>
}
