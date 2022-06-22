package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutItemNewAppointmentsBinding
import com.rootscare.serviceprovider.databinding.LayoutNewItemPaymentSplittingBinding
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.ModelAppointmentsListing
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.models.ModelAppointmentDetails
import com.rootscare.serviceprovider.utilitycommon.setAmount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList


class AdapterPaymentSplitting(internal var context: Context) :
    ListAdapter<ModelAppointmentDetails.Result.TaskDetail, AdapterPaymentSplitting.ViewHolder>(PaymentSolittingDiffUtil()) {

    companion object {
        val TAG: String = AdapterPaymentSplitting::class.java.simpleName
    }
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutNewItemPaymentSplittingBinding>(
            LayoutInflater.from(context),
            R.layout.layout_new_item_payment_splitting, parent, false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.onBindView(getItem(position))
    }

    inner class ViewHolder(val binding: LayoutNewItemPaymentSplittingBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindView(item: ModelAppointmentDetails.Result.TaskDetail?) {
            binding.tvHeading.text = item?.name ?:"Unknown"
           // binding.tvPrice.setAmount(item?.price?:"0")
            binding.tvPrice.text = item?.price?:"0"
        }

    }

}

class PaymentSolittingDiffUtil : DiffUtil.ItemCallback<ModelAppointmentDetails.Result.TaskDetail>() {
    override fun areItemsTheSame(oldItem: ModelAppointmentDetails.Result.TaskDetail, newItem: ModelAppointmentDetails.Result.TaskDetail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ModelAppointmentDetails.Result.TaskDetail, newItem: ModelAppointmentDetails.Result.TaskDetail): Boolean {
        return oldItem == newItem

    }

}