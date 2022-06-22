package com.rootscare.data.model.response.hospital


import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @field:JsonProperty("code")
    @field:SerializedName("code")
    val code: String,
    @field:JsonProperty("message")
    @field:SerializedName("message")
    val message: String,
    @field:JsonProperty("result")
    @field:SerializedName("result")
    val result: ArrayList<NotificationItemResult?>?,
    @field:JsonProperty("status")
    @field:SerializedName("status")
    val status: Boolean
)