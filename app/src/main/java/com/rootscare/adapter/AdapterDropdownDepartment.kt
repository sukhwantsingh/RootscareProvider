package com.rootscare.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.hospital.HospitalResultItem
import com.rootscare.interfaces.OnDepartmentDropDownListItemClickListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDepartmentDropdownBinding
import kotlinx.android.synthetic.main.item_department_dropdown.view.*


class AdapterDropdownDepartment(var departmentList: ArrayList<HospitalResultItem?>?, internal var context: Context) :
    RecyclerView.Adapter<AdapterDropdownDepartment.ViewHolder>() {
    //
    companion object {
        val TAG: String = AdapterCommonDropdown::class.java.simpleName
    }

    internal lateinit var recyclerViewItemClick: OnDepartmentDropDownListItemClickListener
//
//        internal lateinit var recyclerViewItemClickWithView: OnClickWithTwoButton

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDepartmentDropdownBinding>(
            LayoutInflater.from(context),
            R.layout.item_department_dropdown, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return dropdownList!!.size
        return departmentList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDepartmentDropdownBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        init {
            itemView.root.checkbox_department?.setOnCheckedChangeListener { _, isChecked -> //set your object's last status
                //  notifyDataSetChanged()
                if (isChecked) {

                    departmentList?.get(localPosition)?.isChecked = "true"
                    //  notifyDataSetChanged()
                    //                       recyclerViewItemClick.onConfirm(departmentList)
                } else {

                    departmentList?.get(localPosition)?.isChecked = "false"
                    //     notifyDataSetChanged()

                    //                       recyclerViewItemClick.onConfirm(departmentList)
                }

                recyclerViewItemClick.onConfirm(departmentList)
            }


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            localPosition = pos
            itemView.rootView?.txtDropDownDepartment?.text = departmentList?.get(localPosition)?.title
            if (departmentList?.get(localPosition)?.isChecked.equals("false")) {
                itemView.rootView?.checkbox_department?.isChecked = false

            } else if (departmentList?.get(localPosition)?.isChecked.equals("true")) {
                itemView.rootView?.checkbox_department?.isChecked = true
            }


        }
    }

}