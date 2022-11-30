package id.co.mka.narasource.core.domain.repository

import id.co.mka.narasource.core.data.Resource
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun login(param: HashMap<String, String>): Flow<Resource<String>>
    suspend fun register(param: HashMap<String, String>): Flow<Resource<String>>
    fun getSession(): Flow<String?>
    fun getLevel(): Flow<String?>
    suspend fun logout()
}
