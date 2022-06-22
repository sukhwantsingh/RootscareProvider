package com.rootscare.data.model.request.doctor.myscheduletimeslot

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class GetTimeSlotMyScheduleRequest(
    @field:JsonProperty("doctor_id")
    @field:SerializedName("doctor_id")
    var doctor_id: String? = null,
    @field:JsonProperty("clinic_id")
    @field:SerializedName("clinic_id")
    var clinic_id: String? = null,
    @field:JsonProperty("day")
    @field:SerializedName("day")
    var day: String? = null,
    @field:JsonProperty("id")
    @field:SerializedName("id")
    var id: String? = null
)