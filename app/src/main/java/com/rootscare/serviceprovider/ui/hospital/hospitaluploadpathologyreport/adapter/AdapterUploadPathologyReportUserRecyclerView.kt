package com.rootscare.serviceprovider.ui.hospital.hospitaluploadpathologyreport.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.hospital.ReportData
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemHospitalUploadPathologyTestUserRecyclerviewBinding
import kotlinx.android.synthetic.main.item_hospital_upload_pathology_test_user_recyclerview.view.*
import java.util.*

class AdapterUploadPathologyReportUserRecyclerView(
    val reportDataLinkedList: LinkedList<ReportData?>?,
    internal var context: Context
) : RecyclerView.Adapter<AdapterUploadPathologyReportUserRecyclerView.ViewHolder>() {
    companion object {
        val TAG: String = AdapterUploadPathologyReportUserRecyclerView::class.java.simpleName
    }

    internal lateinit var recyclerViewItemClickWithView: OnItemClickWithReportIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterUploadPathologyReportUserRecyclerView.ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemHospitalUploadPathologyTestUserRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_hospital_upload_pathology_test_user_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return reportDataLinkedList!!.size

    }

    override fun onBindViewHolder(holder: AdapterUploadPathologyReportUserRecyclerView.ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemHospitalUploadPathologyTestUserRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        init {
            println("recyclerViewItemClickWithView $recyclerViewItemClickWithView")
            itemView.root.card_view_upload_pathology_user_list?.setOnClickListener {
                recyclerViewItemClickWithView.onItemClick(localPosition)
            }

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
//            println("patient name ${reportDataLinkedList?.get(pos)?.patientName}")
            localPosition = pos

            itemView.rootView?.tv_patient?.text = reportDataLinkedList?.get(pos)?.patientName
            itemView.rootView?.tv_orderId?.text = reportDataLinkedList?.get(pos)?.orderId
            itemView.rootView?.tv_status?.text =
                if (reportDataLinkedList?.get(pos)?.report == "0") ("Report Not Uploaded") else "Report Uploaded"
//            itemView.rootView?.age?.text = reportDataLinkedList?.get(pos)?.age

        }
    }

}