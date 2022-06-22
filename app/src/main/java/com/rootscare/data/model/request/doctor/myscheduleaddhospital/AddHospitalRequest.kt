package com.rootscare.data.model.request.doctor.myscheduleaddhospital

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class AddHospitalRequest(
    @field:JsonProperty("doctor_id")
    @field:SerializedName("doctor_id")
    var doctor_id: String? = null,
    @field:JsonProperty("name")
    @field:SerializedName("name")
    var name: String? = null,
    @field:JsonProperty("address")
    @field:SerializedName("address")
    var address: String? = null
)