package com.rootscare.serviceprovider.ui.pricelistss.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.appevents.codeless.internal.ViewHierarchy.setOnClickListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewItemPackagesBinding
import com.rootscare.serviceprovider.databinding.RowTestIncludedBinding
import com.rootscare.serviceprovider.ui.manageDocLab.model.ModelHospitalDocs
import com.rootscare.serviceprovider.ui.pricelistss.models.ModelPackageDetails
import java.util.ArrayList


class AdapterLabTestsIncluded(internal var context: Context) :
    ListAdapter<ModelPackageDetails.Result.TaskName, AdapterLabTestsIncluded.ViewHolder>(LabTestsIncludedListingDiffUtil()) {

    val updatedArrayList = ArrayList<ModelPackageDetails.Result.TaskName?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RowTestIncludedBinding>(LayoutInflater.from(context), R.layout.row_test_included, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
           holder.onBindView(getItem(position))
    }

    override fun submitList(list: MutableList<ModelPackageDetails.Result.TaskName?>?) {
        super.submitList(if(list.isNullOrEmpty()) null else ArrayList(list))
    }

    inner class ViewHolder(val binding: RowTestIncludedBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
             binding.ivArrowd.setOnClickListener {
                 binding.tvTestDesc.visibility = if(binding.tvTestDesc.isShown) View.GONE else View.VISIBLE
            }
        }

        fun onBindView(item: ModelPackageDetails.Result.TaskName?) {
            binding.run {
                  item?.let {
                    setVariable(BR.node,it)
                    executePendingBindings()
               }
            }
        }

    }
}

class LabTestsIncludedListingDiffUtil : DiffUtil.ItemCallback<ModelPackageDetails.Result.TaskName>() {
    override fun areItemsTheSame(oldItem: ModelPackageDetails.Result.TaskName, newItem: ModelPackageDetails.Result.TaskName): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ModelPackageDetails.Result.TaskName, newItem: ModelPackageDetails.Result.TaskName): Boolean {
        return oldItem == newItem
    }

}