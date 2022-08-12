package com.rootscare.serviceprovider.ui.pricelistss.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewItemPackagesBinding
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPackages

interface OnLabPackageCallback {
    fun onLabPackage(packId: String?)
    fun onLabDisablePackage(packId: String?)
}

class AdapterLabPackages(internal var context: Context) :
    ListAdapter<ModelPackages.Result, AdapterLabPackages.ViewHolder>(LabPackagesListingDiffUtil()) {

    var mCallback: OnLabPackageCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutNewItemPackagesBinding>(
            LayoutInflater.from(context),
            R.layout.layout_new_item_packages, parent, false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.onBindView(getItem(position))
    }

    override fun submitList(list: MutableList<ModelPackages.Result?>?) {
        super.submitList(if (list.isNullOrEmpty()) null else ArrayList(list))
    }

    inner class ViewHolder(val binding: LayoutNewItemPackagesBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.crdMain.setOnClickListener { mCallback?.onLabPackage(getItem(absoluteAdapterPosition).id) }
            binding.swPackEnable.setOnClickListener {
                if(binding.swPackEnable.isChecked) {
                    mCallback?.onLabPackage(getItem(absoluteAdapterPosition).id)
                    binding.swPackEnable.isChecked = false
                } else {
                    mCallback?.onLabDisablePackage(getItem(absoluteAdapterPosition).id)
               }
                
            }
        }

        fun onBindView(item: ModelPackages.Result?) {
            binding.run {
                setVariable(BR.node,item)
                if(item?.isChecked == true) { true.enableDisableViews(binding) } else false.enableDisableViews(binding)
                executePendingBindings()
            }
        }

        private fun Boolean.enableDisableViews(binding: LayoutNewItemPackagesBinding) {
            binding.run {
               if (this@enableDisableViews) {
                    tvPackageName.setTextColor(ContextCompat.getColor(context, R.color.txt_color_profile_desc))
                    tvBankName.setTextColor(ContextCompat.getColor(context, R.color.txt_color_profile_desc))
                    tvTests.setTextColor(ContextCompat.getColor(context, R.color.indicator_color))
                    tvPercentOff.setTextColor(ContextCompat.getColor(context, R.color.green_off_text))
                    tvPercentOff.setBackgroundResource(R.drawable.round_solid_stroke_width_off)

                } else {
                    tvPackageName.setTextColor(ContextCompat.getColor(context, R.color.colorTxtGrey1))
                    tvBankName.setTextColor(ContextCompat.getColor(context, R.color.colorTxtGrey1))
                    tvTests.setTextColor(ContextCompat.getColor(context, R.color.colorTxtGrey1))
                    tvPercentOff.setTextColor(ContextCompat.getColor(context, R.color.colorTxtGrey1))
                    tvPercentOff.setBackgroundResource(R.drawable.round_solid_stroke_with_off_disabled)

                }

            }
        }
    }
}


class LabPackagesListingDiffUtil : DiffUtil.ItemCallback<ModelPackages.Result>() {
    override fun areItemsTheSame(oldItem: ModelPackages.Result, newItem: ModelPackages.Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ModelPackages.Result, newItem: ModelPackages.Result): Boolean {
        return oldItem.id == newItem.id && oldItem.name == newItem.name
    }

}