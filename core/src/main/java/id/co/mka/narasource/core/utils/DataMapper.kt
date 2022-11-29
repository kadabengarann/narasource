package id.co.mka.narasource.core.utils

import id.co.mka.narasource.core.data.source.remote.response.ActivityResponse
import id.co.mka.narasource.core.data.source.remote.response.ArticleResponse
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.domain.model.Article
import id.co.mka.narasource.core.domain.model.NarasumberSession

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
                description = it.description,
                category = it.category,
                price = it.price,
                date = it.date,
                timeStart = it.timeStart,
                timeEnd = it.timeEnd,
                timeStamp = it.timeStamp,
                confirmationStatus = it.confirmationStatus,
                narasumber = NarasumberSession(
                    image = it.narasumber?.image.toString(),
                    id = it.narasumber?.id.toString()
                ),
                meetingId = it.meetingId,
                meetingPassword = it.meetingPassword,
                status = it.status
            )
            dataList.add(item)
        }
        return dataList
    }

    fun mapActivityResponseToDomain(input: ActivityResponse): Activity {
        return Activity(
            id = input.id,
            title = input.title,
            description = input.description,
            category = input.category,
            price = input.price,
            date = input.date,
            timeStart = input.timeStart,
            timeEnd = input.timeEnd,
            timeStamp = input.timeStamp,
            confirmationStatus = input.confirmationStatus,
            narasumber = NarasumberSession(
                image = input.narasumber?.image.toString(),
                id = input.narasumber?.id.toString()
            ),
            meetingId = input.meetingId,
            meetingPassword = input.meetingPassword,
            status = input.status
        )
    }
}
