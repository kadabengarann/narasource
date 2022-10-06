package id.co.mka.naraq.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mka.naraq.core.data.Resource
import id.co.mka.naraq.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    private val _loginResult = MutableLiveData<Resource<String>>()
    val loginResult: LiveData<Resource<String>?> = _loginResult
    fun login(email: String, password: String) = viewModelScope.launch {
        userUseCase.login(email, password).collect(_loginResult::postValue)
    }
}
