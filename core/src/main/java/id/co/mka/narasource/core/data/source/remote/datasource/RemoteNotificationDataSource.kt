package id.co.mka.narasource.core.data.source.remote.datasource

import android.util.Log
import id.co.mka.narasource.core.data.source.remote.network.ApiResponse
import id.co.mka.narasource.core.data.source.remote.network.NotificationService
import id.co.mka.narasource.core.data.source.remote.response.NotificationResponseItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteNotificationDataSource @Inject constructor(retrofit: Retrofit) {
    private val notificationService = retrofit.create(NotificationService::class.java)

    fun getListNotification(isRead: Boolean?): Flow<ApiResponse<List<NotificationResponseItem>>> {
        return flow {
            try {
                emit(ApiResponse.Loading())
                val response = notificationService.getListNotification(isRead)
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}
