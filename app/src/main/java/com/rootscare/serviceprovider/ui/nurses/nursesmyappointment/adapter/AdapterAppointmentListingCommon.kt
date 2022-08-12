package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutItemNewAppointmentsBinding
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.ModelAppointmentsListing
import com.rootscare.serviceprovider.utilitycommon.AppointmentTypes
import com.rootscare.serviceprovider.utilitycommon.LoginTypes
import com.rootscare.serviceprovider.utilitycommon.TransactionStatus
import com.rootscare.serviceprovider.utilitycommon.needToShowVideoCall
import java.util.*
import kotlin.collections.ArrayList

interface OnAppointmentListingCallback {
    fun onItemClick(pos: Int,id: String?) {}
    fun onAcceptBtnClick(id: String, pos: Int, action: String = "", hospId:String?) {}
    fun onRejectBtnBtnClick(id: String, pos: Int, action: String = "", hospId:String?) {}
    fun onVideoCall(dataModel: ModelAppointmentsListing.Result?) {}
}

class AdapterAppointmentListingCommon(internal var context: Context, internal val aptType: String?) :
    ListAdapter<ModelAppointmentsListing.Result, AdapterAppointmentListingCommon.ViewHolder>(AppointmentListingDiffUtil()) {

    companion object {
        val TAG: String = AdapterAppointmentListingCommon::class.java.simpleName
    }

    val updatedArrayList = ArrayList<ModelAppointmentsListing.Result?>()
    internal lateinit var mCallback: OnAppointmentListingCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutItemNewAppointmentsBinding>(
            LayoutInflater.from(context),
            R.layout.layout_item_new_appointments, parent, false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView(getItem(position))
    }

    // dont call this from calling component
    override fun submitList(list: MutableList<ModelAppointmentsListing.Result?>?) {
        super.submitList(if (list.isNullOrEmpty()) null else ArrayList(list))
    }

    fun loadDataIntoList(list: ArrayList<ModelAppointmentsListing.Result?>?) {
        updatedArrayList.clear()
        list?.let { updatedArrayList.addAll(it.toMutableList()) }
        submitList(updatedArrayList)
    }

    fun updateAfterFilterList(list: ArrayList<ModelAppointmentsListing.Result?>?) {
        list?.let { submitList(ArrayList(list)) }
    }

    fun updateMarkAcceptReject(pos: Int?) {
        pos?.let {
            updatedArrayList.removeAt(it)
            submitList(updatedArrayList)  }

    }

    fun getUpdatedList() = updatedArrayList

    inner class ViewHolder(val binding: LayoutItemNewAppointmentsBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                btnViewDetails.setOnClickListener {
                mCallback.onItemClick(absoluteAdapterPosition,getItem(absoluteAdapterPosition)?.id?:"") }
                btnAccept.setOnClickListener {
                    mCallback.onAcceptBtnClick(getItem(absoluteAdapterPosition)?.id?:"",
                        absoluteAdapterPosition, "Accept", getItem(absoluteAdapterPosition).hospital_id.orEmpty())
                }
                ibcross.setOnClickListener {
                    mCallback.onRejectBtnBtnClick(getItem(absoluteAdapterPosition)?.id?:"",
                        absoluteAdapterPosition, "Reject", getItem(absoluteAdapterPosition).hospital_id.orEmpty())
                }
                btnVideoCall.setOnClickListener { mCallback.onVideoCall(getItem(absoluteAdapterPosition)) }
            }
        }

        fun onBindView(item: ModelAppointmentsListing.Result?) {
            binding.run {
                setVariable(BR.node, item)
                when {
                    item?.acceptance_status?.equals(TransactionStatus.PENDING.get(), ignoreCase = true) == true -> {
                        grpAcceptRej.visibility = View.VISIBLE
                        btnVideoCall.visibility = View.GONE
                    }
                    item?.acceptance_status?.equals(TransactionStatus.ACCEPTED.get(), ignoreCase = true) == true -> {
                        grpAcceptRej.visibility = View.GONE

                        if(item.service_type.equals(LoginTypes.DOCTOR.type,ignoreCase = true) &&
                             aptType.equals(AppointmentTypes.ONGOING.get(),ignoreCase = true) &&
                             item.booking_type.equals("online_task",ignoreCase = true)) {
                           // btnVideoCall.visibility = View.VISIBLE
                         needToShowVideoCall(item.app_date, item.app_time, btnVideoCall)
                        } else btnVideoCall.visibility = View.GONE
                    }

                    else -> {
                        btnVideoCall.visibility = View.GONE
                        grpAcceptRej.visibility = View.GONE
                    }
                }
               executePendingBindings()
           }
        }
    }
}

class AppointmentListingDiffUtil : DiffUtil.ItemCallback<ModelAppointmentsListing.Result>() {
    override fun areItemsTheSame(oldItem: ModelAppointmentsListing.Result, newItem: ModelAppointmentsListing.Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ModelAppointmentsListing.Result, newItem: ModelAppointmentsListing.Result): Boolean {
        return oldItem == newItem

    }

}