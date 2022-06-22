package com.rootscare.serviceprovider.ui.physiotherapy.physiotherapyManageProfile.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.caregiver.profileresponse.ReviewRatingItem
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorldetailsReviewlistRecyclewviewBinding
import kotlinx.android.synthetic.main.item_doctorldetails_reviewlist_recyclewview.view.*

class AdapterPhysiotherapyReviewListRecyclerview(
    val reviewRatingList: ArrayList<ReviewRatingItem?>?,
    internal var context: Context
) : RecyclerView.Adapter<AdapterPhysiotherapyReviewListRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
//    val reviewRatingList: ArrayList<ReviewRatingItem?>?,
    companion object {
        val TAG: String = AdapterPhysiotherapyReviewListRecyclerview::class.java.simpleName
    }

//    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
//    internal lateinit var recyclerViewItemClickWithView: RecyclerViewItemClickWithView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorldetailsReviewlistRecyclewviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctorldetails_reviewlist_recyclewview, parent, false
        )
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

        private var localPosition: Int = 0

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            localPosition = pos

            itemView.rootView?.txt_review_provider_name?.text = "Review By:" + " " + reviewRatingList?.get(pos)?.reviewBy
            itemView.rootView?.txt_review?.text = reviewRatingList?.get(pos)?.review
            if (reviewRatingList!![localPosition]?.rating != null && !TextUtils.isEmpty(reviewRatingList[localPosition]?.rating?.trim())) {
                itemView.ratingBardoctordetailseview.rating = reviewRatingList[localPosition]?.rating?.toFloat()!!
            }
//            itemView?.rootView?.txt_home_babysitter_qualification?.setText(babysitterList?.get(pos)?.qualification)
//            Glide.with(context)
//                .load(context.getString(R.string.api_base)+"uploads/images/" + (babysitterList?.get(pos)?.image))
//                .into(itemView?.rootView?.img_babysitter_profile!!)


        }
    }

}