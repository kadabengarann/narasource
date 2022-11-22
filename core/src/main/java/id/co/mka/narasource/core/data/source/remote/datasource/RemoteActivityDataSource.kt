package id.co.mka.narasource.core.data.source.remote.datasource

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import id.co.mka.narasource.core.data.source.remote.network.ActivityService
import id.co.mka.narasource.core.data.source.remote.network.ApiResponse
import id.co.mka.narasource.core.data.source.remote.response.ActivityResponse
import id.co.mka.narasource.core.utils.ActivityFilterType
import id.co.mka.narasource.core.utils.FilterUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteActivityDataSource @Inject constructor(retrofit: Retrofit, @ApplicationContext appContext: Context) {
    private val actifityService = ActivityService(appContext)

    fun getListActivity(filter: ActivityFilterType): Flow<ApiResponse<List<ActivityResponse>>> {
        val filterVal = getFilteredQuery(filter)
        return flow {
            try {
                val response = actifityService.getListActivity(filterVal)
                val dataArray = response
                emit(ApiResponse.Loading())
                delay(1000L)
                if (dataArray.isNotEmpty()) emit(ApiResponse.Success(response)) else emit(ApiResponse.Empty)
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
