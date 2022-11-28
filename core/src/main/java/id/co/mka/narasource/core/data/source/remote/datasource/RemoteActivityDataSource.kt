package id.co.mka.narasource.core.data.source.remote.datasource

import android.util.Log
import id.co.mka.narasource.core.data.source.remote.network.ActivityService
import id.co.mka.narasource.core.data.source.remote.network.ApiResponse
import id.co.mka.narasource.core.data.source.remote.response.ActivityResponse
import id.co.mka.narasource.core.utils.ActivityFilterType
import id.co.mka.narasource.core.utils.FilterUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteActivityDataSource @Inject constructor(retrofit: Retrofit) {
    private val actifityService = retrofit.create(ActivityService::class.java)

    fun getListActivity(filter: ActivityFilterType): Flow<ApiResponse<List<ActivityResponse>>> {
        val filterVal = getFilteredQuery(filter)
        return flow {
            try {
                if (filterVal == -1) {
                    emit(ApiResponse.Loading())
                    val response = actifityService.getListActivity()
                    if (response.isNotEmpty()) emit(ApiResponse.Success(response)) else emit(ApiResponse.Empty)
                } else {
                    emit(ApiResponse.Loading())
                    val response = actifityService.getListActivity(filterVal)
                    if (response.isNotEmpty()) emit(ApiResponse.Success(response)) else emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
    private fun getFilteredQuery(filter: ActivityFilterType): Int {
        return FilterUtils.getFilteredQuery(filter)
    }
}
