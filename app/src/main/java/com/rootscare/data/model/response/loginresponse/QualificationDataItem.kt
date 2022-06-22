package com.rootscare.data.model.response.loginresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class QualificationDataItem(
	@field:JsonProperty("passing_year")
	@field:SerializedName("passing_year")
	val passingYear: String? = null,
	@field:JsonProperty("qualification")
	@field:SerializedName("qualification")
	val qualification: String? = null,
	@field:JsonProperty("institute")
	@field:SerializedName("institute")
	val institute: String? = null,
	@field:JsonProperty("qualification_certificate")
	@field:SerializedName("qualification_certificate")
	val qualificationCertificate: String? = null
)