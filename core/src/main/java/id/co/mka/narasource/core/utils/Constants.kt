package id.co.mka.narasource.core.utils

const val ARG_SECTION_NUMBER = "section_number"

val TAB_ARTICLE_TITLES = arrayOf(
    "Terbaru",
    "A-Z"
)
val TAB_NOTIFICATION_TITLES = arrayOf(
    "Belum dibaca",
    "Sudah dibaca"
)

enum class ArticleFilterType {
    ARTICLE_NEWEST,
    ARTICLE_A_Z,
}

enum class ArticleListType {
    PREVIEW_LIST,
    FULL_LIST
}
