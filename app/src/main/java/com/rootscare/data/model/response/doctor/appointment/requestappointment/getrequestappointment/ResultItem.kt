package com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class ResultItem(
    @field:JsonProperty("appointment_status")
    @field:SerializedName("appointment_status")
    val appointmentStatus: String? = null,
    @field:JsonProperty("booking_date")
    @field:SerializedName("booking_date")
    val bookingDate: String? = null,
    @field:JsonProperty("appointment_date")
    @field:SerializedName("appointment_date")
    val appointmentDate: String? = null,
    @field:JsonProperty("clinic_name")
    @field:SerializedName("clinic_name")
    val clinicName: String? = null,
    @field:JsonProperty("symptom_text")
    @field:SerializedName("symptom_text")
    val symptomText: String? = null,
    @field:JsonProperty("from_time")
    @field:SerializedName("from_time")
    val fromTime: String? = null,
    @field:JsonProperty("acceptance_status")
    @field:SerializedName("acceptance_status")
    var acceptanceStatus: String? = null,
    @field:JsonProperty("symptom_recording")
    @field:SerializedName("symptom_recording")
    val symptomRecording: String? = null,
    @field:JsonProperty("paymentType")
    @field:SerializedName("paymentType")
    val paymentType: String? = null,
    @field:JsonProperty("booked_by")
    @field:SerializedName("booked_by")
    val bookedBy: String? = null,
    @field:JsonProperty("appointment_type")
    @field:SerializedName("appointment_type")
    val appointmentType: String? = null,
    @field:JsonProperty("price")
    @field:SerializedName("price")
    val price: String? = null,
    @field:JsonProperty("upload_prescription")
    @field:SerializedName("upload_prescription")
    val uploadPrescription: String? = null,
    @field:JsonProperty("clinic_address")
    @field:SerializedName("clinic_address")
    val clinicAddress: String? = null,
    @field:JsonProperty("patient_name")
    @field:SerializedName("patient_name")
    val patientName: String? = null,
    @field:JsonProperty("id")
    @field:SerializedName("id")
    val id: String? = null,
    @field:JsonProperty("to_time")
    @field:SerializedName("to_time")
    val toTime: String? = null,
    @field:JsonProperty("doctor_name")
    @field:SerializedName("doctor_name")
    val doctorName: String? = null,
    @field:JsonProperty("order_id")
    @field:SerializedName("order_id")
    val orderId: String? = null,
    @field:JsonProperty("paymentStatus")
    @field:SerializedName("paymentStatus")
    val paymentStatus: String? = null,


    @field:SerializedName("nurse_name")
    val nurseName: String? = null,

    @field:SerializedName("from_date")
    val fromDate: String? = null,

    @field:SerializedName("to_date")
    val toDate: String? = null,

    @field:SerializedName("patient_id")
    val patientId: String? = null,

    @field:SerializedName("nurse_id")
    val nurseId: String? = null
) : Serializable