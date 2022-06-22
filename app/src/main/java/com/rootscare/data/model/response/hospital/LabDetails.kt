package com.rootscare.data.model.response.hospital

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
@JsonIgnoreProperties(ignoreUnknown = true)
data class LabDetails(
    @field:JsonProperty("id")
    @field:SerializedName("id")
    val id: String? = null,
    @field:JsonProperty("name")
    @field:SerializedName("name")
    val name: String? = null,
    @field:JsonProperty("price")
    @field:SerializedName("price")
    val price: String? = null

)