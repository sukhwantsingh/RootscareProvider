package com.rootscare.data.model.response.hospital

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import java.util.*

data class ReportResultItem(
    @field:SerializedName("result")
    val result: LinkedList<ReportData?>? = null,
    @field:JsonProperty("code")
    @field:SerializedName("code")
    val code: String? = null,
    @field:JsonProperty("message")
    @field:SerializedName("message")
    val message: String? = null,
    @field:JsonProperty("status")
    @field:SerializedName("status")
    val status: Boolean? = null
)

data class ReportData(

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("hospital_id")
    val hospitalId: String? = null,

    @field:SerializedName("pathology_test_id")
    val pathologyTestId: String? = null,

    @field:SerializedName("appointment_date")
    val appointmentDate: String? = null,

    @field:SerializedName("from_time")
    val fromTime: String? = null,

    @field:SerializedName("to_time")
    val toTime: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("booking_date")
    val bookingDate: String? = null,

    @field:SerializedName("appointment_status")
    val appointmentStatus: String? = null,

    @field:SerializedName("appointment_id")
    val appointmentId: String? = "",

    @field:SerializedName("acceptance_status")
    val acceptanceStatus: String? = null,

    @field:SerializedName("paymentType")
    val paymentType: String? = null,

    @field:SerializedName("paymentStatus")
    val paymentStatus: String? = null,

    @field:SerializedName("patient_contact")
    val patientContact: String? = null,

    @field:SerializedName("booked_by")
    val bookedBy: String? = null,

    @field:SerializedName("patient_name")
    val patientName: String? = null,

    @field:SerializedName("patient_id")
    val patientId: String? = "",

    @field:SerializedName("hospital_name")
    val hospitalName: String? = "null",

    @field:SerializedName("pathology_name")
    val pathologyName: String? = null,

    @field:SerializedName("report")
    val report: String? = null
)