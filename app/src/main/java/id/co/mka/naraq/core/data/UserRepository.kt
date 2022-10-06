package id.co.mka.naraq.core.data

import id.co.mka.naraq.core.data.source.remote.RemoteUserDataSource
import id.co.mka.naraq.core.data.source.remote.network.ApiResponse
import id.co.mka.naraq.core.data.source.remote.request.LoginRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface IUserRepository {
    suspend fun login(email: String, password: String): Flow<Resource<String>>
}

class UserRepository @Inject constructor(
    private val remoteUserDataSource: RemoteUserDataSource
) : IUserRepository {
    override suspend fun login(email: String, password: String): Flow<Resource<String>> {
        val request = LoginRequest(email, password)
        return remoteUserDataSource.login(request).map {
            when (it) {
                is ApiResponse.Loading -> Resource.Loading()
                is ApiResponse.Success -> Resource.Success(it.data.accessToken)
                is ApiResponse.Error -> Resource.Error(it.errorMessage)
                is ApiResponse.Empty -> Resource.Success("")
            }
        }
    }
}
