package com.rootscare.serviceprovider.ui.hospital.profiledoctorhospital.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.doctor.profileresponse.DepartmentItem
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorldetailsSpecilitylistRecyclerviewBinding
import kotlinx.android.synthetic.main.item_doctorldetails_specilitylist_recyclerview.view.*

class AdapterDoctordetailsSpecilityListRecyclerview (val departmentList: ArrayList<DepartmentItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapterDoctordetailsSpecilityListRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
//    val departmentList: ArrayList<DepartmentItem?>?,
    companion object {
        val TAG: String = AdapterDoctordetailsSpecilityListRecyclerview::class.java.simpleName
    }

//    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
//    internal lateinit var recyclerViewItemClickWithView: RecyclerViewItemClickWithView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorldetailsSpecilitylistRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctorldetails_specilitylist_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return departmentList!!.size
        return departmentList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorldetailsSpecilitylistRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
//            itemView?.root?.btn_send_a_feed_back?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClick?.onClick(trainerList?.get(local_position)?.id!!, trainerList?.get(local_position)?.name!!)
//            })
//            itemView?.root?.btn_view_trainner_profile?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(trainerList?.get(local_position)?.id?.toInt()!!)
//            })

//            itemView.root?.img_bid?.setOnClickListener {
//                run {
//                    recyclerViewItemClick?.onClick(itemView.root?.img_bid,local_position)
//                    //serviceListItem?.get(local_position)?.requestid?.let { it1 -> recyclerViewItemClick.onClick(itemView.root?.img_bid,it1) }
//                }
//            }
//
//            itemView.root?.img_details?.setOnClickListener {
//                run {
//                    recyclerViewItemClick?.onClick(itemView.root?.img_details,local_position)
//                    // serviceListItem?.get(local_position)?.requestid?.let { it1 -> recyclerViewItemClick.onClick(itemView.root?.img_details,it1) }
//                }
//            }


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            itemView?.rootView?.txt_doctordetails_specility?.setText(departmentList?.get(pos)?.title)

        }
    }

}