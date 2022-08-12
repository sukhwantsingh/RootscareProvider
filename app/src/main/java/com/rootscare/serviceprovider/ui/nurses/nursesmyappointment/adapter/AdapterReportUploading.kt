package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.RowItemUploadReportsBinding
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.ModelReportUploading


class AdapterReportUploading :
    ListAdapter<ModelReportUploading, AdapterReportUploading.ViewHolder>(ReportUploadingDiffUtil()) {

    val updatedArrayList: MutableList<ModelReportUploading?> = java.util.ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RowItemUploadReportsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_item_upload_reports_, parent, false
        )
        return ViewHolder(binding)
    }

    override fun submitList(list: MutableList<ModelReportUploading?>?) {
        super.submitList(if (list.isNullOrEmpty()) null else ArrayList(list))
    }

    fun loadDataNodeIntoList(node: ModelReportUploading?) {
        node?.let {
            updatedArrayList.add(node)
            submitList(updatedArrayList)
        }
    }

    fun removeNode(pos: Int) {
        updatedArrayList.removeAt(pos)
        submitList(updatedArrayList)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView(getItem(position))
    }

    inner class ViewHolder(val binding: RowItemUploadReportsBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ivcross.setOnClickListener { removeNode(absoluteAdapterPosition) }
        }


        fun onBindView(item: ModelReportUploading?) {
             binding.tvPrescName.text = item?.reportName.orEmpty()
        }

    }

}

class ReportUploadingDiffUtil : DiffUtil.ItemCallback<ModelReportUploading>() {
    override fun areItemsTheSame(oldItem: ModelReportUploading, newItem: ModelReportUploading): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ModelReportUploading, newItem: ModelReportUploading): Boolean {
        return oldItem == newItem

    }

}