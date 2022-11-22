package id.co.mka.narasource.core.domain.usecase

import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.data.repository.ActivityRepository
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.utils.ActivityFilterType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ActivityUseCase {
    fun getListActivity(filter: ActivityFilterType): Flow<Resource<List<Activity>>>
    fun getDetailActivity()
}
class ActivityInteractor @Inject constructor(
    private val activityRepository: ActivityRepository
) : ActivityUseCase {
    override fun getListActivity(filter: ActivityFilterType) = activityRepository.getListActivity(filter)
    override fun getDetailActivity() {
    }
}
