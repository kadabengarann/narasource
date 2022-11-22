package id.co.mka.narasource.presentation.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mka.narasource.core.domain.usecase.UserUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    suspend fun logout() = userUseCase.logout()
}
