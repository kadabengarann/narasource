package id.co.mka.naraq.core.data

import id.co.mka.naraq.core.data.source.local.SessionService
import id.co.mka.naraq.core.data.source.remote.RemoteUserDataSource
import id.co.mka.naraq.core.data.source.remote.network.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface IUserRepository {
    suspend fun login(param: HashMap<String, String>): Flow<Resource<String>>
    suspend fun register(param: HashMap<String, String>): Flow<Resource<String>>
    fun getSession(): Flow<String?>
    suspend fun logout()
}

class UserRepository @Inject constructor(
    private val remoteUserDataSource: RemoteUserDataSource,
    private val sessionService: SessionService
) : IUserRepository {
    override suspend fun login(param: HashMap<String, String>): Flow<Resource<String>> {
        return remoteUserDataSource.login(param).map {
            when (it) {
                is ApiResponse.Loading -> Resource.Loading()
                is ApiResponse.Success -> {
                    sessionService.setToken(it.data.accessToken)
                    Resource.Success(it.data.accessToken)
                }
                is ApiResponse.Error -> Resource.Error(it.errorMessage)
                is ApiResponse.Empty -> Resource.Success("")
            }
        }
    }

    override suspend fun register(param: HashMap<String, String>): Flow<Resource<String>> {
        return remoteUserDataSource.register(param).map {
            when (it) {
                is ApiResponse.Loading -> Resource.Loading()
                is ApiResponse.Success -> Resource.Success(it.data.accessToken)
                is ApiResponse.Error -> Resource.Error(it.errorMessage)
                is ApiResponse.Empty -> Resource.Success("")
            }
        }
    }
    override fun getSession(): Flow<String?> = sessionService.getToken()
    override suspend fun logout() = withContext(Dispatchers.IO) { sessionService.logout() }
}
