package id.co.mka.narasource.core.data.repository

import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.data.source.remote.datasource.RemoteActivityDataSource
import id.co.mka.narasource.core.data.source.remote.network.ApiResponse
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.domain.repository.IActivityRepository
import id.co.mka.narasource.core.utils.ActivityFilterType
import id.co.mka.narasource.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ActivityRepository @Inject constructor(
    private val remoteActivityDataSource: RemoteActivityDataSource
) : IActivityRepository {
    override fun getListActivity(filter: ActivityFilterType): Flow<Resource<List<Activity>>> {
        return remoteActivityDataSource.getListActivity(filter).map {
            when (it) {
                is ApiResponse.Success -> {
                    val data = DataMapper.mapActivityResponsesToDomain(it.data)
                    Resource.Success(data)
                }
                is ApiResponse.Empty -> Resource.Success(emptyList())
                is ApiResponse.Error -> Resource.Error(it.errorMessage)
                is ApiResponse.Loading -> Resource.Loading()
            }
        }
    }

    override fun getDetailActivity(id: Int): Flow<Resource<Activity>> {
        return remoteActivityDataSource.getDetailActivity(id).map {
            when (it) {
                is ApiResponse.Success -> {
                    val data = DataMapper.mapActivityResponseToDomain(it.data)
                    Resource.Success(data)
                }
                is ApiResponse.Error -> Resource.Error(it.errorMessage)
                is ApiResponse.Loading -> Resource.Loading()
                else -> {
                    Resource.Error("Error")
                }
            }
        }
    }
}
