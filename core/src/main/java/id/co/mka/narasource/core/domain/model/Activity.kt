package id.co.mka.narasource.core.domain.model

data class Activity(
    val id: Int,
    val title: String,
    val category: String,
    val description: String,
    val price: Int,
    val date: String,
    val timeStart: String,
    val timeEnd: String,
    val timeStamp: String,
    val confirmationStatus: String,
    val narasumber: NarasumberSession?,
    val meetingId: String?,
    val meetingPassword: String?,
    val status: Int
)

data class NarasumberSession(
    val image: String,
    val id: String
)
