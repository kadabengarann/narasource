package id.co.mka.narasource.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mka.narasource.core.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    val level: LiveData<String?> = userUseCase.getLevel().stateIn(viewModelScope, SharingStarted.Lazily, null).asLiveData()
    suspend fun logout() = userUseCase.logout()
}
