package com.rootscare.data.model.response.hospital

import com.google.gson.annotations.SerializedName
import java.util.*

data class ResponseLabPastAppointment(

	@field:SerializedName("result")
	var result: LinkedList<ResultItemLab>? = null,

	@field:SerializedName("code")
	val code: String? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: Boolean? = null
)

data class ResultItemLab(

	@field:SerializedName("appointment_status")
	val appointmentStatus: String? = null,

	@field:SerializedName("booking_date")
	val bookingDate: String? = null,

	@field:SerializedName("appointment_date")
	val appointmentDate: String? = null,

	@field:SerializedName("clinic_name")
	val clinicName: String? = null,

	@field:SerializedName("symptom_text")
	val symptomText: String? = null,

	@field:SerializedName("from_time")
	val fromTime: String? = null,

	@field:SerializedName("acceptance_status")
	val acceptanceStatus: String? = null,

	@field:SerializedName("symptom_recording")
	val symptomRecording: String? = null,

	@field:SerializedName("paymentType")
	val paymentType: String? = null,

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

	@field:SerializedName("first_name")
	val firstName: String? = null,

	@field:SerializedName("last_name")
	val lastName: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("to_time")
	val toTime: String? = null,

	@field:SerializedName("doctor_name")
	val doctorName: String? = null,

	@field:SerializedName("order_id")
	val order_id: String? = null,

	@field:SerializedName("amount")
	val amount: String? = null,

	@field:SerializedName("paymentStatus")
	val paymentStatus: String? = null,

	@field:SerializedName("hospital_name")
	val hospitalName: String? = null,

	@field:SerializedName("nurse_name")
	val nurseName: String? = null,

	@field:SerializedName("from_date")
	val fromDate: String? = null,

	@field:SerializedName("to_date")
	val toDate: String? = null,

	@field:SerializedName("patient_id")
	val patientId: String? = null,

	@field:SerializedName("nurse_id")
	val nurseId: String? = null,

	var date: Date? = null
)
