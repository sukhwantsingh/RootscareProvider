package com.rootscare.data.model.request.doctor.myscheduleaddtimeslot

import com.google.gson.annotations.SerializedName
import java.util.*

data class AddTimeSlotRequest(

	@field:SerializedName("doctor_id")
	var doctorId: String? = null,

	@field:SerializedName("slot")
	var slot: LinkedList<SlotItem>? = null,

	@field:SerializedName("clinic_id")
	var clinicId: String? = null,

	@field:SerializedName("day")
	var day: String? = null,

	@field:SerializedName("hospital_id")
	var hospitalId: String? = null,

	@field:SerializedName("department_id")
	var departmentId: String? = null,
)

data class SlotItem(

	@field:SerializedName("time_to")
	var timeTo: String? = null,

	@field:SerializedName("time_from")
	var timeFrom: String? = null
)
