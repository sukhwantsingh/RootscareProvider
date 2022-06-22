package com.rootscare.data.model.response.doctor.myschedule.hospitallist

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class MyScheduleHospitalResponse(

	@field:SerializedName("result")
	val result: LinkedList<ResultItem>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
) : Serializable

data class ResultItem(

	@field:SerializedName("doctor_id")
	val doctorId: String? = null,

	@field:SerializedName("address")
	val address: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null
) : Serializable
