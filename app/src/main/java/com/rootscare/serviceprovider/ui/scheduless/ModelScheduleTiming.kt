package com.rootscare.serviceprovider.ui.scheduless


import androidx.annotation.Keep

@Keep
data class ModelScheduleTiming(
    val code: String?,
    val message: String?,
    val result: Result?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val accept_booking: String?,
        val service_address: String?,
        val service_lat: String?,
        val service_long: String?,
        val service_radius: String?,
        val slots: ArrayList<Slot?>?
    ) {
        @Keep
        data class Slot(
            val slot_day: String?,
            val slot_day_enable: String?,
            val slot_end_time: String?,
            val slot_start_time: String?
        )
    }
}