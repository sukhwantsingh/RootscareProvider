package com.rootscare.serviceprovider.ui.transactionss.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewItemPaymentHisotryBinding
import com.rootscare.serviceprovider.ui.transactionss.models.ModelTransactions
import com.rootscare.serviceprovider.utilitycommon.backgroundStatusColor


class AdapterPayTransactions(internal var context: Context) :
    ListAdapter<ModelTransactions.Result.Paymentdetail, AdapterPayTransactions.ViewHolder>(PayTransactionDiffUtil()) {

    companion object {
        val TAG: String = AdapterPayTransactions::class.java.simpleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutNewItemPaymentHisotryBinding>(
            LayoutInflater.from(context),
            R.layout.layout_new_item_payment_hisotry, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView(getItem(position))
    }

    inner class ViewHolder(val binding: LayoutNewItemPaymentHisotryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBindView(data: ModelTransactions.Result.Paymentdetail?) {
            binding.run {
                tvIdValue.text = data?.order_id ?: ""
                tvDateValue.text = data?.date
                tvAmountValue.text = data?.price ?: ""
                tvPaymentTypeValue.text = data?.paymentType ?: ""
                tvPaymentStatusValue.text = data?.paymentStatus ?: ""

                tvPaymentStatusValue.backgroundStatusColor(data?.paymentStatus)
            }
        }

    }


}

class PayTransactionDiffUtil : DiffUtil.ItemCallback<ModelTransactions.Result.Paymentdetail>() {
    override fun areItemsTheSame(oldItem: ModelTransactions.Result.Paymentdetail, newItem: ModelTransactions.Result.Paymentdetail): Boolean {
        return oldItem.order_id == newItem.order_id
    }

    override fun areContentsTheSame(oldItem: ModelTransactions.Result.Paymentdetail, newItem: ModelTransactions.Result.Paymentdetail): Boolean {
        return oldItem == newItem
    }

}