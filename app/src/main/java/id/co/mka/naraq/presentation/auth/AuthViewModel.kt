package id.co.mka.naraq.presentation.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mka.naraq.core.data.Resource
import id.co.mka.naraq.core.domain.usecase.UserUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {
    private val _loginResult = MutableLiveData<Resource<String>>()
    val loginResult: LiveData<Resource<String>?> = _loginResult
    private val _registerResult = MutableLiveData<Resource<String>>()
    val registerResult: LiveData<Resource<String>?> = _registerResult
    val session: LiveData<String?> = userUseCase.getSession().stateIn(viewModelScope, SharingStarted.Lazily, null).asLiveData()
    var isChecked = false

    fun login(email: String, password: String) = viewModelScope.launch {
        val param = HashMap<String, String>()
        param["email"] = email
        param["password"] = password
        userUseCase.login(param).collect(_loginResult::postValue)
    }

    fun register(name: String, userName: String, email: String, password: String) = viewModelScope.launch {
        val param = HashMap<String, String>()
        param["name"] = name
        param["username"] = userName
        param["email"] = email
        param["password"] = password
        userUseCase.register(param).collect(_registerResult::postValue)
    }
}
