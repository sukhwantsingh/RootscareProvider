package com.rootscare.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.interfaces.OnClickWithTwoButton
import com.rootscare.interfaces.OnDropDownListItemClickListener
import com.rootscare.model.RowItem
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemCommonDropdownListBinding
import kotlinx.android.synthetic.main.item_common_dropdown_list.view.*

class AdapterCommonDropdown ( val dropdownList: ArrayList<String?>?,internal var context: Context) : RecyclerView.Adapter<AdapterCommonDropdown.ViewHolder>() {
            internal lateinit var recyclerViewItemClick: OnDropDownListItemClickListener
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemCommonDropdownListBinding>(
                LayoutInflater.from(context),
                R.layout.item_common_dropdown_list, parent, false)
            return ViewHolder(singleItemDashboardListingBinding)
        }

        override fun getItemCount(): Int {
        return dropdownList?.size ?: 0
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.onBind(position)
        }


        inner class ViewHolder(itemView: ItemCommonDropdownListBinding) : RecyclerView.ViewHolder(itemView.root) {

            private var local_position:Int = 0
            init {
                itemView?.root?.ll_dropdown_list?.setOnClickListener(View.OnClickListener {
                    recyclerViewItemClick?.onConfirm(dropdownList?.get(local_position)!!)
                })

            }

            fun onBind(pos: Int) {
                local_position = pos
                itemView?.rootView?.txtDropDownListLabel?.setText(dropdownList?.get(local_position))


            }
        }

    }