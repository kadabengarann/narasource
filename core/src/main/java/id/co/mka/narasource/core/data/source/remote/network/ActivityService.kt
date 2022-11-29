package id.co.mka.narasource.core.data.source.remote.network

import id.co.mka.narasource.core.data.source.remote.response.ActivityResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActivityService {
    @GET("activity")
    suspend fun getListActivity(
        @Query("status") query: Int? = null
    ): List<ActivityResponse>

    @GET("activity/{id}")
    suspend fun getDetailActivity(
        @Path("id") id: Int
    ): ActivityResponse
}
