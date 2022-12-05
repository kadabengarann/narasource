package id.co.mka.narasource.narasumber.presentation.activity

import android.util.Log
import androidx.lifecycle.*
import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.domain.usecase.ActivityUseCase
import id.co.mka.narasource.core.utils.ActivityFilterType

class ActivityListViewModel(
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
        Log.d("TAGAGAGA", "getData: $filter")
        return activityUseCase.getListActivity(filter).asLiveData()
    }
}
