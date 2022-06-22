package com.rootscare.data.model.request.loginrequest

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@Keep
@JsonIgnoreProperties(ignoreUnknown = true)
data class LoginRequest(
	@field:JsonProperty("password")
	@field:SerializedName("password")
    var password: String? = null,
	@field:JsonProperty("user_type")
	@field:SerializedName("user_type")
	var userType: String? = null,
	@field:JsonProperty("email")
	@field:SerializedName("email")
	var email: String? = null,
	@field:JsonProperty("device_id")
	@field:SerializedName("device_id")
	var device_id: String? = null,
	@field:JsonProperty("fcm_token")
	@field:SerializedName("fcm_token")
	var fcm_token: String? = null,
	@field:JsonProperty("device_type")
	@field:SerializedName("device_type")
	var device_type: String? = null
)