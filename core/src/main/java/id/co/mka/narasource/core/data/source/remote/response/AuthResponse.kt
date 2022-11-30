package id.co.mka.narasource.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class AuthResponse(
    @field:SerializedName("access_token")
    val accessToken: String,

    @field:SerializedName("level_access")
    val level: String,

    @field:SerializedName("message")
    val message: String
)
