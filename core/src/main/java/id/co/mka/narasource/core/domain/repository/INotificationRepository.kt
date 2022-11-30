package id.co.mka.narasource.core.domain.repository

import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Notification
import kotlinx.coroutines.flow.Flow

interface INotificationRepository {
    fun getListNotification(isRead: Boolean): Flow<Resource<List<Notification>>>
}
