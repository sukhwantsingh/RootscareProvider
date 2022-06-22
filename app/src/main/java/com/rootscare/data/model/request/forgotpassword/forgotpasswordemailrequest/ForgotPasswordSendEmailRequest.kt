package com.rootscare.data.model.request.forgotpassword.forgotpasswordemailrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class ForgotPasswordSendEmailRequest(
	@field:JsonProperty("emailId")
	@field:SerializedName("emailId")
	var emailId: String? = null
)