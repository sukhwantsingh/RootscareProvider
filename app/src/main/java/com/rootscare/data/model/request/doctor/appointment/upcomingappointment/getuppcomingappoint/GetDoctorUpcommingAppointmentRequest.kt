package com.rootscare.data.model.request.doctor.appointment.upcomingappointment.getuppcomingappoint

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetDoctorUpcommingAppointmentRequest(
	@field:JsonProperty("user_id")
	@field:SerializedName("user_id")
	var userId: String? = null,

	@field:JsonProperty("doctor_id")
	@field:SerializedName("doctor_id")
	var doctorId: String? = null
)