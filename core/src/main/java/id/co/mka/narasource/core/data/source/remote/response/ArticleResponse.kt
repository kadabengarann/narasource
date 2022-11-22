package id.co.mka.narasource.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class ArticleResponse(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("date_created")
    val dateCreated: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("author")
    val author: String,

    @field:SerializedName("content")
    val content: String,

    @field:SerializedName("image")
    val image: String
)
