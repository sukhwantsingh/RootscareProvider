package com.rootscare.serviceprovider.ui.nurses.nurseprofile.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNweItemHspDepartsBinding
import com.rootscare.serviceprovider.databinding.RowEditableDepartmentsBinding
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelHospDeparts

class AdapterHospitalDepartments(val departList: ArrayList<String>?, internal var context: Context
) : RecyclerView.Adapter<AdapterHospitalDepartments.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutNweItemHspDepartsBinding>(
            LayoutInflater.from(context),
            R.layout.layout_nwe_item_hsp_departs, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return departList?.size ?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(internal var binding: LayoutNweItemHspDepartsBinding) : RecyclerView.ViewHolder(binding.root) {

       fun onBind(pos: Int) {
         binding.txtName.text = departList?.get(pos).orEmpty()
         binding.executePendingBindings()
        }
    }

}


interface OnDepartListingCallback {
    fun onDelDepart()
}
class AdapterSelectedDepartments :
    ListAdapter<ModelHospDeparts.Result, AdapterSelectedDepartments.ViewHolder>(SelectedDepartListDiffUtil()) {

    var updatedArrayList: MutableList<ModelHospDeparts.Result?>? = java.util.ArrayList()
    var mCallback :OnDepartListingCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<RowEditableDepartmentsBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_editable_departments, parent, false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView(updatedArrayList?.get(position))
    }

    override fun submitList(list: MutableList<ModelHospDeparts.Result?>?) {
        super.submitList(if (list.isNullOrEmpty()) null else ArrayList(list))
    }

    fun loadDataIntoList(node: ModelHospDeparts.Result?) {
        node?.let {
            updatedArrayList?.add(0,node)
            submitList(updatedArrayList)
        }
    }

    fun loadAllDataIntoList(node: ArrayList<ModelHospDeparts.Result?>?) {
        node?.let {
            updatedArrayList = node
            submitList(updatedArrayList)
        }
    }

    fun removeNode(node: ModelHospDeparts.Result?) {
        node?.let {
            updatedArrayList?.remove(node)
            submitList(updatedArrayList)
        }
        mCallback?.onDelDepart()
    }

    inner class ViewHolder(val binding: RowEditableDepartmentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.imgCancel.setOnClickListener {
                removeNode(updatedArrayList?.get(absoluteAdapterPosition))
            }
        }

        fun onBindView(item: ModelHospDeparts.Result?) {
            binding.run {
                tvTask.text = item?.title ?: ""
            }
        }

    }

}

class SelectedDepartListDiffUtil : DiffUtil.ItemCallback<ModelHospDeparts.Result>() {
    override fun areItemsTheSame(oldItem: ModelHospDeparts.Result, newItem: ModelHospDeparts.Result): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: ModelHospDeparts.Result, newItem: ModelHospDeparts.Result): Boolean {
        return oldItem == newItem
    }

}