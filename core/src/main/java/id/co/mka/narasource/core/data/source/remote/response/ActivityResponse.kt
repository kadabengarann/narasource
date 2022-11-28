package id.co.mka.narasource.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class ActivityResponse(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("confirmation_status")
    val confirmationStatus: String,

    @field:SerializedName("time_start")
    val timeStart: String,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("narasumber")
    val narasumber: NarasumberSession?,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("created_at")
    val timeStamp: String,

    @field:SerializedName("time_end")
    val timeEnd: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("status")
    val status: Int,

    @field:SerializedName("meeting_id")
    val meetingId: String?,

    @field:SerializedName("meeting_password")
    val meetingPassword: String?
)

data class NarasumberSession(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("id")
    val id: String
)
