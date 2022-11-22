package id.co.mka.narasource.core.domain.repository

import id.co.mka.narasource.core.data.Resource
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.utils.ActivityFilterType
import kotlinx.coroutines.flow.Flow

interface IActivityRepository {
    fun getListActivity(filter: ActivityFilterType): Flow<Resource<List<Activity>>>
}
