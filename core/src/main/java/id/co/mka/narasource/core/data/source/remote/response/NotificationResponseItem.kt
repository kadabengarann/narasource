package id.co.mka.narasource.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class NotificationResponseItem(

    @field:SerializedName("user_type")
    val userType: String,

    @field:SerializedName("read")
    val read: Boolean,

    @field:SerializedName("user_id")
    val userId: Int,

    @field:SerializedName("destination_id")
    val destinationId: Int,

    @field:SerializedName("created_at")
    val timeStamp: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("body")
    val description: String
)
