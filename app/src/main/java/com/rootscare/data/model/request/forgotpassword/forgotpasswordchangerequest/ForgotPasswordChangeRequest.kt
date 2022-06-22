package com.rootscare.data.model.request.forgotpassword.forgotpasswordchangerequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class ForgotPasswordChangeRequest(
	@field:JsonProperty("password")
	@field:SerializedName("password")
	var password: String? = null,
	@field:JsonProperty("code")
	@field:SerializedName("code")
	var code: String? = null,
	@field:JsonProperty("emailId")
	@field:SerializedName("emailId")
    var emailId: String? = null
)