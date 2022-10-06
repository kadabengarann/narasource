package id.co.mka.naraq.core.data.source.remote

import android.util.Log
import id.co.mka.naraq.core.data.source.remote.network.ApiResponse
import id.co.mka.naraq.core.data.source.remote.network.UserService
import id.co.mka.naraq.core.data.source.remote.request.LoginRequest
import id.co.mka.naraq.core.data.source.remote.response.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteUserDataSource @Inject constructor(retrofit: Retrofit) {
    private val userService = retrofit.create(UserService::class.java)

    suspend fun login(request: LoginRequest): Flow<ApiResponse<LoginResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading())
                val response = userService.login(request)
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        emit(ApiResponse.Success(it))
                    } ?: run {
                        emit(ApiResponse.Error("${response.code()} ${response.message()}"))
                    }
                } else {
                    if (response.code() == 401) {
                        emit(ApiResponse.Error("Username atau password salah"))
                    } else {
                        emit(ApiResponse.Error("${response.code()} ${response.message()}"))
                    }
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}
