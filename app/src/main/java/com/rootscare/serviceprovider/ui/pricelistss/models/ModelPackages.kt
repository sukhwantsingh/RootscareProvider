package com.rootscare.serviceprovider.ui.pricelistss.models


import androidx.annotation.Keep

@Keep
data class ModelPackages(
    val code: String?,
    val message: String?,
    val result: ArrayList<Result?>?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val dis_off: String?,
        val display_rp: String?,
        val id: String?,
        val isChecked: Boolean?,
        val maxrp: String?,
        val strikeEnable: Boolean?,
        val minrp: String?,
        val name: String?,
        val price: String?,
        val task_count: String?,
        val task_details: String?
    )
}