package id.co.mka.naraq.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

class LoginResponse(
    @field:SerializedName("access_token")
    val accessToken: String
)
