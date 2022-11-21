package id.co.mka.narasource.presentation.activity

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.domain.usecase.ActivityUseCase
import id.co.mka.narasource.core.utils.ActivityFilterType
import javax.inject.Inject

@HiltViewModel
class ActivityListViewModel @Inject constructor(
    private val activityUseCase: ActivityUseCase
) : ViewModel() {
    private val _filter = MutableLiveData<ActivityFilterType>()

    val activities: LiveData<Resource<List<Activity>>> = _filter.switchMap {
        getData(it)
    }

    fun filter(filterType: ActivityFilterType) {
        _filter.value = filterType
    }
    private fun getData(filter: ActivityFilterType): LiveData<Resource<List<Activity>>> {
        return activityUseCase.getListActivity(filter).asLiveData()
    }
}
