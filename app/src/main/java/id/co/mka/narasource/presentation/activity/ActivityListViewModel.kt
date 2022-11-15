package id.co.mka.narasource.presentation.activity

import androidx.lifecycle.*
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.utils.ActivityFilterType
import id.co.mka.narasource.core.utils.FilterUtils

class ActivityListViewModel : ViewModel() {
    private val _filter = MutableLiveData<ActivityFilterType>()

    val activities: LiveData<MutableList<Activity>> = _filter.switchMap {
        getData(it)
    }

    fun filter(filterType: ActivityFilterType) {
        _filter.value = filterType
    }

    private fun getFilteredQuery(filter: ActivityFilterType): Int {
        return FilterUtils.getFilteredQuery(filter)
    }
    private fun getData(filter: ActivityFilterType): LiveData<MutableList<Activity>> {
        val query = getFilteredQuery(filter)
        val listData: MutableList<Activity> = mutableListOf()
        for (i in 1..8) {
            val article = Activity(
                name = "Title",
                category = "UI/UX Design",
                timeStamp = "11 Oktober 2022, 15.00 WIB",
                status = (0..2).random()
            )
            listData.add(article)
        }
        return if (query < 0) {
            MutableLiveData(listData)
        } else {
            val filteredList = listData.filter { it.status == query }
            MutableLiveData(filteredList.toMutableList())
        }
    }
}
