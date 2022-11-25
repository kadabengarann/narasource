package id.co.mka.narasource.core.domain.model

data class Payment(
    val id: Int,
    val name: String,
    val image: Int,
    var selected: Boolean = false
)
