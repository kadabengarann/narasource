package id.co.mka.narasource.core.data.repository

import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.data.source.remote.datasource.RemoteNotificationDataSource
import id.co.mka.narasource.core.data.source.remote.network.ApiResponse
import id.co.mka.narasource.core.domain.model.Notification
import id.co.mka.narasource.core.domain.repository.INotificationRepository
import id.co.mka.narasource.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NotificationRepository @Inject constructor(
    private val remoteNotificationDataSource: RemoteNotificationDataSource
) : INotificationRepository {
    override fun getListNotification(isRead: Boolean): Flow<Resource<List<Notification>>> {
        return remoteNotificationDataSource.getListNotification(isRead).map {
            when (it) {
                is ApiResponse.Success -> {
                    val data = DataMapper.mapNotificationResponsesToEntities(it.data)
                    Resource.Success(data)
                }
                is ApiResponse.Empty -> Resource.Success(emptyList())
                is ApiResponse.Error -> Resource.Error(it.errorMessage)
                is ApiResponse.Loading -> Resource.Loading()
            }
        }
    }
}
