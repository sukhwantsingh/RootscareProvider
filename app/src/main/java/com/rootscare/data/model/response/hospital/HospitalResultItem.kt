package com.rootscare.data.model.response.hospital

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class HospitalResultItem(
	@field:JsonProperty("id")
	@field:SerializedName("id")
	val id: String? = null,
	@field:JsonProperty("title")
	@field:SerializedName("title")
	val title: String? = null,
	@field:JsonProperty("image")
	@field:SerializedName("image")
	val image: String? = null,
	var isChecked: String = "false"

)