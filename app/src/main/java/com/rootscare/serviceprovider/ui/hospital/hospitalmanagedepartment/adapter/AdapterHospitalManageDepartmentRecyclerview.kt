package com.rootscare.serviceprovider.ui.hospital.hospitalmanagedepartment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rootscare.data.model.response.hospital.HospitalResultItem
import com.rootscare.interfaces.OnItemClickHospitalListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemHospitalManageDepartmentRecyclerviewBinding
import com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.adapter.AdapterReviewAndRatingRecyclerview
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls
import kotlinx.android.synthetic.main.item_hospital_manage_department_recyclerview.view.*

class AdapterHospitalManageDepartmentRecyclerview(
    internal var todaysAppointList: ArrayList<HospitalResultItem?>?,
    internal var context: Context
) : RecyclerView.Adapter<AdapterHospitalManageDepartmentRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterReviewAndRatingRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClickHospitalListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemHospitalManageDepartmentRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_hospital_manage_department_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return todaysAppointList!!.size
        //  return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemHospitalManageDepartmentRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        init {
            itemView.root.edit?.setOnClickListener {
                recyclerViewItemClickWithView.onItemClick(
                    todaysAppointList?.get(localPosition)?.id?.toInt()!!,
                    todaysAppointList?.get(localPosition)?.title!!
                )
            }
            itemView.root.delete?.setOnClickListener {
                recyclerViewItemClickWithView.onSecondItemClick(
                    todaysAppointList?.get(localPosition)?.id?.toInt()!!
                )
            }


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            localPosition = pos

            itemView.rootView?.depart_name?.text = todaysAppointList?.get(pos)?.title

            val circularProgressDrawable = CircularProgressDrawable(context)

            val requestOptions: RequestOptions =
                RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.department_img)
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
            val imageUrl =
                BaseMediaUrls.USERIMAGE.url + todaysAppointList?.get(pos)!!.image
            Glide.with(context).load(imageUrl).apply(RequestOptions().placeholder(circularProgressDrawable)).timeout(1000 * 60)
                .apply(requestOptions).into(itemView.image_profile)


        }
    }

}