package id.co.mka.naraq.core.domain.usecase

import id.co.mka.naraq.core.data.IUserRepository
import id.co.mka.naraq.core.data.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserUseCase {
    suspend fun login(param: HashMap<String, String>): Flow<Resource<String>>
    suspend fun register(param: HashMap<String, String>): Flow<Resource<String>>
    fun getSession(): Flow<String?>
    suspend fun logout()
}

class UserInteractor @Inject constructor(private val userRepository: IUserRepository) :
    UserUseCase {
    override suspend fun login(param: HashMap<String, String>) = userRepository.login(param)
    override suspend fun register(param: HashMap<String, String>): Flow<Resource<String>> = userRepository.register(param)
    override fun getSession(): Flow<String?> = userRepository.getSession()
    override suspend fun logout() = userRepository.logout()
}
