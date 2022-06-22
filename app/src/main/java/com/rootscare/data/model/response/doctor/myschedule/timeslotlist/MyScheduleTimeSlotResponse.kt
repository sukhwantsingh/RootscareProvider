package com.rootscare.data.model.response.doctor.myschedule.timeslotlist

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

data class MyScheduleTimeSlotResponse(

	@field:SerializedName("result")
	val result: LinkedList<ResultItem>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ResultItem(

	@field:SerializedName("doctor_id")
	val doctorId: String? = null,

	@field:SerializedName("slot")
	val slot: ArrayList<Slot>? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("clinic_id")
	val clinicId: String? = null,

	@field:SerializedName("day")
	val day: String? = null
)

data class Slot(
	@field:SerializedName("time_to")
	val timeTo: String? = null,

	@field:SerializedName("time_from")
	val timeFrom: String? = null
)
