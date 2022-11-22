package id.co.mka.narasource.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class ActivityResponse(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("category")
    val category: String,

    @field:SerializedName("created_at")
    val timeStamp: String,

    @field:SerializedName("status")
    val status: Int
)
