package com.rootscare.data.model.response.caregiver.profileresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import java.io.File

@JsonIgnoreProperties(ignoreUnknown = true)
data class QualificationDataItem(
	@field:JsonProperty("passing_year")
	@field:SerializedName("passing_year")
	var passingYear: String? = null,
	@field:JsonProperty("qualification")
	@field:SerializedName("qualification")
	var qualification: String? = null,
	@field:JsonProperty("institute")
	@field:SerializedName("institute")
	var institute: String? = null,
	@field:JsonProperty("qualification_certificate")
	@field:SerializedName("qualification_certificate")
	var qualificationCertificate: String? = null,
	
	var isOldData:Boolean = false,
	var certificateFileTemporay:File? = null
)