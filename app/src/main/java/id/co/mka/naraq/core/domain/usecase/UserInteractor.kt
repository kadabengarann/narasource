package id.co.mka.naraq.core.domain.usecase

import id.co.mka.naraq.core.data.IUserRepository
import id.co.mka.naraq.core.data.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface UserUseCase {
    suspend fun login(email: String, password: String): Flow<Resource<String>>
}

class UserInteractor @Inject constructor(private val userRepository: IUserRepository) : UserUseCase {
    override suspend fun login(email: String, password: String) = userRepository.login(email, password)
}
