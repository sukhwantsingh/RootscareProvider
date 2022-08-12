package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.RowItemReportsForAppointmentDetailsBinding
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.ModelAppointmentDetails
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.ModelLabReports

interface OnLabReportsCallback {
    fun onDownloads(mFileName: String)
}

class AdapterReportUploadeds :
    ListAdapter<ModelAppointmentDetails.Result.LabTestReports, AdapterReportUploadeds.ViewHolder>(ReportUploadedDiffUtil()) {

    var mCallback: OnLabReportsCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RowItemReportsForAppointmentDetailsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_item_reports_for_appointment_details, parent, false
        )
        return ViewHolder(binding)
    }

    override fun submitList(list: MutableList<ModelAppointmentDetails.Result.LabTestReports?>?) {
        super.submitList(if (list.isNullOrEmpty()) null else ArrayList(list))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView(getItem(position))
    }

    inner class ViewHolder(val binding: RowItemReportsForAppointmentDetailsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.tvPrescDowonload.setOnClickListener {
                mCallback?.onDownloads(getItem(absoluteAdapterPosition).report.orEmpty())
            }
        }

        fun onBindView(item: ModelAppointmentDetails.Result.LabTestReports?) {
            binding.tvPrescName.text = item?.report.orEmpty()
            binding.tvPrescDate.text = "${item?.upload_date.orEmpty()}"
        }

    }

}

class ReportUploadedDiffUtil : DiffUtil.ItemCallback<ModelAppointmentDetails.Result.LabTestReports>() {
    override fun areItemsTheSame(oldItem: ModelAppointmentDetails.Result.LabTestReports, newItem: ModelAppointmentDetails.Result.LabTestReports): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ModelAppointmentDetails.Result.LabTestReports, newItem: ModelAppointmentDetails.Result.LabTestReports): Boolean {
        return oldItem == newItem

    }

}