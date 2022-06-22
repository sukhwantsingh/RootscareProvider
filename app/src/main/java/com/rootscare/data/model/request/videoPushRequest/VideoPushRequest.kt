package com.rootscare.data.model.request.videoPushRequest

import androidx.annotation.Keep
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@Keep
@JsonIgnoreProperties(ignoreUnknown = true)
data class VideoPushRequest(
    @field:JsonProperty("fromUserId")
    @field:SerializedName("fromUserId")
    var fromUserId: String? = null,
    @field:JsonProperty("toUserId")
    @field:SerializedName("toUserId")
    var toUserId: String? = null,
    @field:JsonProperty("order_id")
    @field:SerializedName("order_id")
    var orderId: String? = null,
    @field:JsonProperty("room_name")
    @field:SerializedName("room_name")
    var roomName: String? = null,
    @field:JsonProperty("fromUserName")
    @field:SerializedName("fromUserName")
    var fromUserName: String? = null,
    @field:JsonProperty("toUserName")
    @field:SerializedName("toUserName")
    var toUserName: String? = null,
    @field:JsonProperty("type")
    @field:SerializedName("type")
    var type: String? = null
)