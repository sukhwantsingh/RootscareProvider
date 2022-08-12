package com.rootscare.serviceprovider.ui.pricelistss.models


import androidx.annotation.Keep

@Keep
data class ModelPackageDetails(
    val code: String?,
    val message: String?,
    val result: Result?,
    val status: Boolean?
) {
    @Keep
    data class Result(
        val display_rp: String?,
        val maxrp: String?,
        val minrp: String?,
        val name: String?,
        val pid: String?,
        val price: String?,
        val task_content: String?,
        val task_count: String?,
        val task_heading: String?,
        val task_name: ArrayList<TaskName?>?,
        val task_sub_content: String?,
        val task_sub_heading: String?
    ) {
        @Keep
        data class TaskName(
            val name: String?,
            val subtask: String?
        ) {
         //   fun showSubTasks() = if(subtask.isNullOrBlank().not()) subtask?.split(",")?.joinToString(separator = "\n") else ""
          //  fun showSubTasks() = "diabetes,hippor,creatine,Nepalianactriate".split(",")?.joinToString(separator = "<br>")
           fun showSubTasks() = subtask.orEmpty()
        }
    }
}