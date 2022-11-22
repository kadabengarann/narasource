package id.co.mka.narasource.core.domain.model

data class Article(
    val id: String,
    val title: String,
    val dateCreated: String,
    val category: String,
    val author: String,
    val content: String,
    val image: String
)
