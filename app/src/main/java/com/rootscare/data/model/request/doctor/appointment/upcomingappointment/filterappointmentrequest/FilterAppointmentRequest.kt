package com.rootscare.data.model.request.doctor.appointment.upcomingappointment.filterappointmentrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class FilterAppointmentRequest(
	@field:JsonProperty("user_id")
	@field:SerializedName("user_id")
	var userId: String? = null,
	@field:JsonProperty("appointment_date")
	@field:SerializedName("appointment_date")
	var appointmentDate: String? = null,
	@field:JsonProperty("from_date")
	@field:SerializedName("from_date")
	var fromDate: String? = null
)