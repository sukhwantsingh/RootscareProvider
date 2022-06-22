package com.rootscare.data.model.response.hospital

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

@JsonIgnoreProperties(ignoreUnknown = true)
data class OrderResult(
	@field:JsonProperty("pathology_test_id")
	@field:SerializedName("pathology_test_id")
	val pathology_test_id: String? = null,
	@field:JsonProperty("lab")
	@field:SerializedName("lab")
	val qualificationData: ArrayList<LabDetails?>? = null,
	@field:JsonProperty("family_member_id")
	@field:SerializedName("family_member_id")
	val family_member_id: String? = null,
	@field:JsonProperty("patient_id")
	@field:SerializedName("patient_id")
	val patient_id: String? = null,

	@field:JsonProperty("appointment_date")
	@field:SerializedName("appointment_date")
	val appointment_date: String? = null,
	@field:JsonProperty("order_id")
	@field:SerializedName("order_id")
	val order_id: String? = null,
	@field:JsonProperty("first_name")
	@field:SerializedName("first_name")
	val first_name: String? = null,
	@field:JsonProperty("last_name")
	@field:SerializedName("last_name")
	val last_name: String? = null,
	@field:JsonProperty("image")
	@field:SerializedName("image")
	val image: String? = null,
	@field:JsonProperty("email")
	@field:SerializedName("email")
	val email: String? = null,
	@field:JsonProperty("phone_number")
	@field:SerializedName("phone_number")
	val phone_number: String? = null,
	@field:JsonProperty("gender")
	@field:SerializedName("gender")
	val gender: String? = null,
	@field:JsonProperty("age")
	@field:SerializedName("age")
	val age: String? = null,
	@field:JsonProperty("from_time")
	@field:SerializedName("from_time")
	val from_time: String? = null,
	@field:JsonProperty("to_time")
	@field:SerializedName("to_time")
	val to_time: String? = null,
	@field:JsonProperty("amount")
	@field:SerializedName("amount")
	val amount: String? = null



	// for nurse (extra params)

)