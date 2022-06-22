package com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutItemNewQualificationMoreBinding
import com.rootscare.serviceprovider.databinding.LayoutNewItemWithdrawbleBinding
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelQualificationMore
import com.rootscare.serviceprovider.ui.transactionss.adapters.AdapterWithdrawal
import com.rootscare.serviceprovider.ui.transactionss.adapters.WithdrawalDiffUtil
import com.rootscare.serviceprovider.ui.transactionss.models.ModelWithdrawalDetails


class AdapterQualificationMore(internal var context: Context) : ListAdapter<ModelQualificationMore,
            AdapterQualificationMore.ViewHolder>(MoreQualiDiffUtil()) {

    internal var callback: OnItemClickWithReportIdListener? = null

    //assume you have this either in your view model or where ever it is
    val dataList = ArrayList<ModelQualificationMore>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutItemNewQualificationMoreBinding>(
            LayoutInflater.from(context),
            R.layout.layout_item_new_qualification_more, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView(getItem(position))
    }

    fun updateData(newData: List<ModelQualificationMore>){
        dataList.addAll(newData) // add new data to existing data
        submitList(ArrayList(dataList)) // submit the data again
    }

    inner class ViewHolder(val binding: LayoutItemNewQualificationMoreBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvhBcUploadPhotocopy.setOnClickListener { callback?.uploadCerti(binding.tvBcNaId) }
        }

        fun onBindView(item: ModelQualificationMore?) {
            binding.run {
                edtQuali.setText(item?.qualifiaction)
                tvBcNaId.text = item?.qualiFileName
          }
       }
    }
}

class MoreQualiDiffUtil : DiffUtil.ItemCallback<ModelQualificationMore>() {
    override fun areItemsTheSame(oldItem: ModelQualificationMore, newItem: ModelQualificationMore): Boolean {
        return oldItem==newItem
    }

    override fun areContentsTheSame(oldItem: ModelQualificationMore, newItem: ModelQualificationMore): Boolean {
        return oldItem == newItem
    }

}