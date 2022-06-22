package com.rootscare.data.model.response.doctor.myschedule.homeVisitList


import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

data class HomeVisitSchedule(
    @SerializedName("code")
    val code: String? = null,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("result")
    val result: LinkedList<Result>? = null,
    @SerializedName("status")
    val status: Boolean? = null
) : Serializable

data class Result(
    @SerializedName("day")
    val day: String? = null,
    @SerializedName("doctor_id")
    val doctorId: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("slot")
    val slot: LinkedList<Slot>? = null
) : Serializable

data class Slot(
    @SerializedName("slot_id")
    val slotId: String? = null,
    @SerializedName("time_from")
    val timeFrom: String? = null,
    @SerializedName("time_to")
    val timeTo: String? = null
) : Serializable
