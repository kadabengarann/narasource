package id.co.mka.narasource.core.utils

import id.co.mka.narasource.core.data.source.remote.response.ActivityResponse
import id.co.mka.narasource.core.data.source.remote.response.ArticleResponse
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.domain.model.Article

object DataMapper {
    fun mapArticleResponsesToDomain(input: List<ArticleResponse>): List<Article> {
        val articleList = ArrayList<Article>()
        input.map {
            val article = Article(
                id = it.id,
                title = it.title,
                dateCreated = it.dateCreated,
                category = it.category,
                author = it.author,
                content = it.content,
                image = it.image
            )
            articleList.add(article)
        }
        return articleList
    }

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
