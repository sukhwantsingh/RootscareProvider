package com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.hospital.ResultSetLab
import com.rootscare.interfaces.OnItemClikWithIdListenerMain
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemHospitalSampleCollectionRecyclerviewBinding
import kotlinx.android.synthetic.main.item_hospital_sample_collection_recyclerview.view.*
import java.util.*

class AdapterHospitalSampleCollectionRecyclerview(
    val upcomingAppointmentList: LinkedList<ResultSetLab?>?,
    internal var context: Context
) : RecyclerView.Adapter<AdapterHospitalSampleCollectionRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalSampleCollectionRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListenerMain

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemHospitalSampleCollectionRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_hospital_sample_collection_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return upcomingAppointmentList!!.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemHospitalSampleCollectionRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        init {
/*
            itemView?.root?.card_view_collection_list?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView?.onItemClick(1)
            })
*/
            itemView.root.view_details?.setOnClickListener {
                recyclerViewItemClickWithView.onItemClick(upcomingAppointmentList?.get(localPosition)?.order_id!!)
            }

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
            localPosition = pos

            itemView.rootView?.order_id?.text = upcomingAppointmentList?.get(pos)?.order_id
            itemView.rootView?.name_lab?.text =
                upcomingAppointmentList?.get(pos)?.first_name + " " + upcomingAppointmentList?.get(pos)?.last_name
            itemView.rootView?.age?.text = upcomingAppointmentList?.get(pos)?.age


//            itemView?.rootView?.txt_rating_count?.text="("+contactListItem?.get(pos)?.contactRating+")"
//            (contactListItem?.get(pos)?.contactRating)?.toFloat()?.let { itemView?.rootView?.ratingBar?.setRating(it) }
////            itemView?.rootView?.ratingBar?.rating=1.5f


        }
    }

}