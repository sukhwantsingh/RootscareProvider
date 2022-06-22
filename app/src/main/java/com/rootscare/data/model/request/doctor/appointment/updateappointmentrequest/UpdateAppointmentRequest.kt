package com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class UpdateAppointmentRequest(
	@JsonIgnoreProperties(ignoreUnknown = true)
	@field:SerializedName("id")
	var id: String? = null,
	@JsonIgnoreProperties(ignoreUnknown = true)
	@field:SerializedName("acceptance_status")
	var acceptanceStatus: String? = null
)