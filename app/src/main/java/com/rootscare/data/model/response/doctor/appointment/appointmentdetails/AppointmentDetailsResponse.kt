package com.rootscare.data.model.response.doctor.appointment.appointmentdetails

import com.google.gson.annotations.SerializedName
import java.util.*

data class AppointmentDetailsResponse(

    @field:SerializedName("result")
    val result: Result? = null,

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Boolean? = null
)

data class Result(

    @field:SerializedName("appointment_status")
    val appointmentStatus: String? = null,


    @field:SerializedName("booking_date")
    val bookingDate: String? = null,

    @field:SerializedName("appointment_date")
    val appointmentDate: String? = null,

    @field:SerializedName("doctor_experience")
    val doctorExperience: String? = null,

    @field:SerializedName("clinic_name")
    val clinicName: String? = null,

    @field:SerializedName("patient_id")
    var patient_id: String? = null,

    @field:SerializedName("symptom_text")
    val symptomText: String? = null,

    @field:SerializedName("doctor_image")
    val doctorImage: String? = null,

    @field:SerializedName("from_time")
    val fromTime: String? = null,

    @field:SerializedName("acceptance_status")
    val acceptanceStatus: String? = null,

    @field:SerializedName("symptom_recording")
    val symptomRecording: String? = null,

    @field:SerializedName("paymentType")
    val paymentType: String? = null,

    @field:SerializedName("patient_contact")
    val patientContact: String? = null,

    @field:SerializedName("booked_by")
    val bookedBy: String? = null,

    @field:SerializedName("appointment_type")
    val appointmentType: String? = null,

    @field:SerializedName("price")
    val price: String? = null,

    @field:SerializedName("upload_prescription")
    val uploadPrescription: String? = null,

    @field:SerializedName("clinic_address")
    val clinicAddress: String? = null,

    @field:SerializedName("patient_name")
    val patientName: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("to_time")
    val toTime: String? = null,

    @field:SerializedName("doctor_name")
    val doctorName: String? = null,

    @field:SerializedName("order_id")
    val orderId: String? = null,

    @field:SerializedName("paymentStatus")
    val paymentStatus: String? = null,

    @field:SerializedName("prescription")
    val prescription: LinkedList<Prescription>? = null,


    @field:SerializedName("nurse_name")
    val nurseName: String? = null,

    @field:SerializedName("nurse_experience")
    val nurseExperience: String? = null,

    @field:SerializedName("from_date")
    val fromDate: String? = null,

    @field:SerializedName("to_date")
    val toDate: String? = null,

    @field:SerializedName("nurse_image")
    val nurseImage: String? = null,

	@field:SerializedName("physiotherapist_name")
	val physiotherapistName: String? = null,

    @field:SerializedName("physiotherapist_image")
    val physiotherapistImage: String? = null
,

    @field:SerializedName("task_details")
    val taskDetails: TaskDetails? = null

)


data class Prescription(

    @field:SerializedName("prescription_number")
    val prescription_number: String? = null,

    @field:SerializedName("e_prescription")
    val e_prescription: String? = null
)

data class TaskDetails(

    @field:SerializedName("test_ids")
    val test_ids: String? = null,

    @field:SerializedName("test_name")
    val test_name: String? = null,

    @field:SerializedName("test_price")
    val test_price: String? = null
)
