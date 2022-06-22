package com.rootscare.data.model.request.hospital

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class EditNew(
    @field:JsonProperty("hospital_id")
    @field:SerializedName("hospital_id")
    var hospital_id: String? = null,
    @field:JsonProperty("department_id")
    @field:SerializedName("department_id")
    var department_id: Int? = null,
    @field:JsonProperty("title")
@field:SerializedName("title")
var title: String? = null
)