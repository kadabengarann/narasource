package id.co.mka.narasource.core.domain.model

data class Session(
    val session: Int,
    var date: String? = null,
    var timeStart: String? = null,
    var timeEnd: String? = null
)
