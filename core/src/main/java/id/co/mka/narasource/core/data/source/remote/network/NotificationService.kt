package id.co.mka.narasource.core.data.source.remote.network

import id.co.mka.narasource.core.data.source.remote.response.NotificationResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationService {
    @GET("notification")
    suspend fun getListNotification(
        @Query("isRead") isRead: Boolean? = null
    ): List<NotificationResponseItem>
}
