package id.co.mka.narasource.narasumber

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import id.co.mka.narasource.core.domain.usecase.ActivityUseCase
import id.co.mka.narasource.narasumber.presentation.activity.ActivityDetailViewModel
import id.co.mka.narasource.narasumber.presentation.activity.ActivityListViewModel
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val activityUseCase: ActivityUseCase) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(ActivityListViewModel::class.java) -> {
                ActivityListViewModel(activityUseCase) as T
            }
            modelClass.isAssignableFrom(ActivityDetailViewModel::class.java) -> {
                ActivityDetailViewModel(activityUseCase) as T
            }
            else -> throw Throwable("Unknown ViewModel class: " + modelClass.name)
        }
}
