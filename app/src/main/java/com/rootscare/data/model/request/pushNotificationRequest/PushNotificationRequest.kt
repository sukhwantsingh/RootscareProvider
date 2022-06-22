package com.rootscare.data.model.request.pushNotificationRequest

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class PushNotificationRequest(
    @field:JsonProperty("user_id")
    @field:SerializedName("user_id")
    var userId: String? = null,
    @field:JsonProperty("user_type")
    @field:SerializedName("user_type")
    var userType: String? = null,
    @field:JsonProperty("notification_for")
    @field:SerializedName("notification_for")
    var notificationFor: String? = null,
    @field:JsonProperty("message")
    @field:SerializedName("message")
    var message: String? = null,
    @field:JsonProperty("name")
    @field:SerializedName("name")
    var name: String? = null
)