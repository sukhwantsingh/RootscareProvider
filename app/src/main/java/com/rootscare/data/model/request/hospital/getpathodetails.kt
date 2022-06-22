package com.rootscare.data.model.request.hospital

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@Keep
@JsonIgnoreProperties(ignoreUnknown = true)
data class getpathodetails(
    @field:JsonProperty("order_id")
    @field:SerializedName("order_id")
    var order_id: String? = null
)