package id.co.mka.narasource.core.domain.usecase

import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.data.repository.NotificationRepository
import id.co.mka.narasource.core.domain.model.Notification
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface NotificationUseCase {
    fun getListNotification(isRead: Boolean): Flow<Resource<List<Notification>>>
}
class NotificationInteractor @Inject constructor(
    private val notificationRepository: NotificationRepository
) : NotificationUseCase {
    override fun getListNotification(isRead: Boolean): Flow<Resource<List<Notification>>> {
        return notificationRepository.getListNotification(isRead)
    }
}
