package id.co.mka.narasource.core.utils

import id.co.mka.narasource.core.data.source.remote.response.ActivityResponse
import id.co.mka.narasource.core.domain.model.Activity

object DataMapper {

    fun mapActivityResponsesToDomain(input: List<ActivityResponse>): List<Activity> {
        val dataList = ArrayList<Activity>()
        input.map {
            val item = Activity(
                id = it.id,
                title = it.title,
                category = it.category,
                timeStamp = it.timeStamp,
                status = it.status
            )
            dataList.add(item)
        }
        return dataList
    }
}
