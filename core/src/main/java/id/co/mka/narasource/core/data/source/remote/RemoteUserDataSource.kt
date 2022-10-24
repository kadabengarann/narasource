package id.co.mka.narasource.core.data.source.remote

import android.util.Log
import com.google.gson.Gson
import id.co.mka.narasource.core.data.source.remote.network.ApiResponse
import id.co.mka.narasource.core.data.source.remote.network.UserService
import id.co.mka.narasource.core.data.source.remote.response.AuthResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteUserDataSource @Inject constructor(retrofit: Retrofit) {
    private val userService = retrofit.create(UserService::class.java)

    suspend fun login(param: HashMap<String, String>): Flow<ApiResponse<AuthResponse>> =
        getResponse { userService.login(param) }

    suspend fun register(param: HashMap<String, String>): Flow<ApiResponse<AuthResponse>> =
        getResponse { userService.register(param) }

    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>
    ): Flow<ApiResponse<T>> {
        return flow {
            try {
                emit(ApiResponse.Loading())
                val response = request.invoke()
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        emit(ApiResponse.Success(it))
                    } ?: run {
                        emit(ApiResponse.Error("${response.code()} ${response.message()}"))
                    }
                } else {
                    if (response.code() == 401) {
                        val message = Gson().fromJson(response.errorBody()?.string(), AuthResponse::class.java)
                        emit(ApiResponse.Error(message.message))
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
