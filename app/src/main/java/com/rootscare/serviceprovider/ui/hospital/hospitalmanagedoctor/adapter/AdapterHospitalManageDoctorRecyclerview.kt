package com.rootscare.serviceprovider.ui.hospital.hospitalmanagedoctor.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rootscare.data.model.response.hospital.ResultItemDoctor
import com.rootscare.interfaces.OnItemClickWithIdListenerHos
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemHospitalManageDoctorRecyclerviewBinding
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls
import kotlinx.android.synthetic.main.item_hospital_manage_doctor_recyclerview.view.*

class AdapterHospitalManageDoctorRecyclerview(internal var todaysAppointList: ArrayList<ResultItemDoctor?>?, internal var context: Context) :
    RecyclerView.Adapter<AdapterHospitalManageDoctorRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalManageDoctorRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClickWithIdListenerHos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemHospitalManageDoctorRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_hospital_manage_doctor_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return todaysAppointList!!.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemHospitalManageDoctorRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        init {
            itemView.root.edit_profile?.setOnClickListener {
                recyclerViewItemClickWithView.onItemClick(todaysAppointList?.get(localPosition)?.userId!!)
            }
            itemView.root.edit_profile_new?.setOnClickListener {
                recyclerViewItemClickWithView.onItemClickSecond(todaysAppointList?.get(localPosition)?.userId!!)
            }
            itemView.root.btn_delete?.setOnClickListener {
                recyclerViewItemClickWithView.onItemClickThird(todaysAppointList?.get(localPosition)?.userId!!)
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

            itemView.rootView?.doc_name?.text = todaysAppointList?.get(pos)?.firstName + " " + todaysAppointList?.get(pos)?.lastName
            itemView.rootView?.qualification?.text = todaysAppointList?.get(pos)?.qualification
            if (todaysAppointList?.get(pos)?.image != null && !todaysAppointList?.get(pos)?.image.equals("")) {
                Glide.with(context)
                    .load(BaseMediaUrls.USERIMAGE.url + (todaysAppointList?.get(pos)?.image))
                    .into(itemView.rootView?.image_profile!!)
            } else {
                Glide.with(context)
                    .load(R.drawable.doctor_profile_photo)
                    .into(itemView.rootView?.image_profile!!)
            }
            Glide.with(context)
                .load(BaseMediaUrls.USERIMAGE.url + (todaysAppointList?.get(pos)?.image))
                .into(itemView.rootView?.image_profile!!)


//            itemView?.rootView?.txt_rating_count?.text="("+contactListItem?.get(pos)?.contactRating+")"
//            (contactListItem?.get(pos)?.contactRating)?.toFloat()?.let { itemView?.rootView?.ratingBar?.setRating(it) }
////            itemView?.rootView?.ratingBar?.rating=1.5f


        }
    }

}