package com.rootscare.data.model.request.twilio

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
data class TwilioAccessTokenRequest (
    @field:JsonProperty("identity")
    @field:SerializedName("identity")
    var identity: String? = null,
    @field:JsonProperty("room_name")
    @field:SerializedName("room_name")
    var roomName: String? = null
)