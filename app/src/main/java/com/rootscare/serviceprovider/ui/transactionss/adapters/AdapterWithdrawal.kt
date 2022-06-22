package com.rootscare.serviceprovider.ui.transactionss.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewItemWithdrawbleBinding
import com.rootscare.serviceprovider.ui.transactionss.models.ModelWithdrawalDetails


class AdapterWithdrawal(internal var context: Context) :
    ListAdapter<ModelWithdrawalDetails.Result.Withdrawal, AdapterWithdrawal.ViewHolder>(WithdrawalDiffUtil()) {

    companion object {
        val TAG: String = AdapterWithdrawal::class.java.simpleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutNewItemWithdrawbleBinding>(
            LayoutInflater.from(context),
            R.layout.layout_new_item_withdrawble_, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView(getItem(position))
    }

    inner class ViewHolder(val binding: LayoutNewItemWithdrawbleBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindView(item: ModelWithdrawalDetails.Result.Withdrawal?) {
            binding.run {
                tvHeading.text = item?.text
                tvPrice.text = item?.amount
                tvDate.text = item?.date

            }
        }

    }


}

class WithdrawalDiffUtil : DiffUtil.ItemCallback<ModelWithdrawalDetails.Result.Withdrawal>() {
    override fun areItemsTheSame(oldItem: ModelWithdrawalDetails.Result.Withdrawal, newItem: ModelWithdrawalDetails.Result.Withdrawal): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ModelWithdrawalDetails.Result.Withdrawal, newItem: ModelWithdrawalDetails.Result.Withdrawal): Boolean {
        return oldItem == newItem
    }

}