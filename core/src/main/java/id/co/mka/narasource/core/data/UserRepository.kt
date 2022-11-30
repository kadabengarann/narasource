package id.co.mka.narasource.core.data

import id.co.mka.narasource.core.data.source.local.SessionService
import id.co.mka.narasource.core.data.source.remote.RemoteUserDataSource
import id.co.mka.narasource.core.data.source.remote.network.ApiResponse
import id.co.mka.narasource.core.domain.repository.IUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

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
                    sessionService.setLevel(it.data.level)
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
    override fun getLevel(): Flow<String?> = sessionService.getLevel()
    override suspend fun logout() = withContext(Dispatchers.IO) { sessionService.logout() }
}
