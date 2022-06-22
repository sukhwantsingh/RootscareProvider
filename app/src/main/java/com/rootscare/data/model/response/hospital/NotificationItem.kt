package com.rootscare.data.model.response.hospital

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

data class NotificationItemResult(
    @field:JsonProperty("body")
    @field:SerializedName("body")
    val body: String,
    @field:JsonProperty("date")
    @field:SerializedName("date")
    val date: String,
    @field:JsonProperty("datetime")
    @field:SerializedName("datetime")
    val datetime: String,
    @field:JsonProperty("id")
    @field:SerializedName("id")
    val id: String,
    @field:JsonProperty("order_id")
    @field:SerializedName("order_id")
    val orderId: String,
    @field:JsonProperty("read")
    @field:SerializedName("read")
    var read: String,
    @field:JsonProperty("title")
    @field:SerializedName("title")
    val title: String,
    @field:JsonProperty("user_id")
    @field:SerializedName("user_id")
    val userId: String
)