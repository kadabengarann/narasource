package id.co.mka.narasource.core.domain.model

data class Notification(
    val id: Int,
    val title: String,
    val description: String,
    val timeStamp: String,
    val userId: Int,
    val userType: String,
    val destinationId: Int?,
    val isRead: Boolean
)
