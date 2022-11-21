package id.co.mka.narasource.core.data.source.remote.network

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import id.co.mka.narasource.core.data.source.remote.response.ArticleResponse
import java.io.IOException

class ArticleService(private val context: Context) {
    fun getPreviewArticle() = getPreviewArticleData().take(4)
    private fun getPreviewArticleData(): List<ArticleResponse> {
        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("sample_article.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        val listArticle = object : TypeToken<List<ArticleResponse>>() {}.type
        return Gson().fromJson(jsonString, listArticle)
    }

    fun searchArticle(): List<ArticleResponse> {
        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("sample_article.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        val listArticle = object : TypeToken<List<ArticleResponse>>() {}.type
        return Gson().fromJson(jsonString, listArticle)
    }

    fun getDetailArticle(id: String): ArticleResponse {
        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("sample_article.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }

        val listArticle = object : TypeToken<List<ArticleResponse>>() {}.type
        val article = Gson().fromJson(jsonString, listArticle) as List<ArticleResponse>
        return article.find { it.id == id }!!
    }
}
