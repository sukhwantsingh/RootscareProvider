package com.rootscare.serviceprovider.ui.hospital.profiledoctorhospital.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.doctor.profileresponse.ReviewRatingItem
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorldetailsReviewlistRecyclewviewBinding
import kotlinx.android.synthetic.main.item_doctorldetails_reviewlist_recyclewview.view.*

class AdapterDoctordetailsReviewListRecyclerview (val reviewRatingList: ArrayList<ReviewRatingItem?>?,internal var context: Context) : RecyclerView.Adapter<AdapterDoctordetailsReviewListRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
//    val reviewRatingList: ArrayList<ReviewRatingItem?>?,
    companion object {
        val TAG: String = AdapterDoctordetailsReviewListRecyclerview::class.java.simpleName
    }

//    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
//    internal lateinit var recyclerViewItemClickWithView: RecyclerViewItemClickWithView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorldetailsReviewlistRecyclewviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctorldetails_reviewlist_recyclewview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return reviewRatingList!!.size
        return reviewRatingList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorldetailsReviewlistRecyclewviewBinding) : RecyclerView.ViewHolder(itemView.root) {

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

            itemView?.rootView?.txt_review_provider_name?.setText("Review By:"+" "+reviewRatingList?.get(pos)?.reviewBy)
            itemView?.rootView?.txt_review?.setText(reviewRatingList?.get(pos)?.review)
            if (reviewRatingList!![local_position]?.rating!=null && !TextUtils.isEmpty(reviewRatingList!![local_position]?.rating?.trim())){
                itemView.ratingBardoctordetailseview.rating = reviewRatingList[local_position]?.rating?.toFloat()!!
            }
//            itemView?.rootView?.txt_home_babysitter_qualification?.setText(babysitterList?.get(pos)?.qualification)
//            Glide.with(context)
//                .load(context.getString(R.string.api_base)+"uploads/images/" + (babysitterList?.get(pos)?.image))
//                .into(itemView?.rootView?.img_babysitter_profile!!)




        }
    }

}