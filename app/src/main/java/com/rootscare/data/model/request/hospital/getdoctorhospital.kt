package com.rootscare.data.model.request.hospital

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class getdoctorhospital(
    @field:JsonProperty("hospital_id")
    @field:SerializedName("hospital_id")
    var hospital_id: String? = null
)