package id.co.mka.narasource.narasumber.presentation.activity

import androidx.lifecycle.*
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.domain.usecase.ActivityUseCase
import kotlinx.coroutines.launch

class ActivityDetailViewModel(
    private val activityUseCase: ActivityUseCase
) : ViewModel() {
    private val _detailActivity = MutableLiveData<Resource<Activity>>()
    val detailActivity: LiveData<Resource<Activity>> = _detailActivity

    var ratingVal = MutableLiveData<Float>()
    var ratingDesc = MutableLiveData<String>()

    fun getDetailActivity(id: Int) {
        viewModelScope.launch {
            activityUseCase.getDetailActivity(id).collect(_detailActivity::postValue)
        }
    }
}
