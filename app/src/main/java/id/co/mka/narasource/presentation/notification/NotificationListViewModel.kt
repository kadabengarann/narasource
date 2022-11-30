package id.co.mka.narasource.presentation.notification

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Notification
import id.co.mka.narasource.core.domain.usecase.NotificationUseCase
import id.co.mka.narasource.core.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel @Inject constructor(
    private val notificationUseCase: NotificationUseCase,
    private val userUseCase: UserUseCase
) : ViewModel() {
    private val _isRead = MutableLiveData<Boolean>()
    private val _notifications = MutableLiveData<Resource<List<Notification>>>()
    val notifications: LiveData<Resource<List<Notification>>> = _notifications

    fun getNotification(isRead: Boolean) {
        _isRead.value = isRead
        // flow to MutableLiveData
        notificationUseCase.getListNotification(isRead).asLiveData().observeForever {
            _notifications.value = it
        }
    }

    val level: LiveData<String?> = userUseCase.getLevel().stateIn(viewModelScope, SharingStarted.Lazily, null).asLiveData()
}
