package id.co.mka.narasource.core.data.source.remote.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.co.mka.narasource.core.data.source.remote.response.ActivityResponse
import java.io.IOException

class ActivityService(private val context: Context) {

    fun getListActivity(query: Int): List<ActivityResponse> {
        val dataList = getListActivityJson()
        if (query == -1) return dataList
        return dataList.filter { it.status == query }
    }
    private fun getListActivityJson(): List<ActivityResponse> {
        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("sample_activity.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        val listArticle = object : TypeToken<List<ActivityResponse>>() {}.type
        return Gson().fromJson(jsonString, listArticle)
    }
}
