package com.rootscare.serviceprovider.ui.doctor.doctorreviewandrating.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rootscare.data.model.response.doctor.review.ResultItem

import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemReviewAndRatingRecyclerviewBinding
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls
import java.util.*

class AdapterReviewAndRatingRecyclerview  ( internal var context: Context) : RecyclerView.Adapter<AdapterReviewAndRatingRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterReviewAndRatingRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener
    var result: LinkedList<ResultItem> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemReviewAndRatingRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_review_and_rating_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(var itemVie: ItemReviewAndRatingRecyclerviewBinding) : RecyclerView.ViewHolder(itemVie.root) {

        private var local_position:Int = 0
        init {
//            itemView?.root?.crdview_doctor_category_list?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(1)
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

            with(itemVie){

                if (result[local_position].rating!=null && !TextUtils.isEmpty(result[local_position].rating?.trim())){
                    ratingBarteacherFeedback.rating = result[local_position].rating?.toFloat()!!
                }

                if (result[local_position].image!=null && !TextUtils.isEmpty(result[local_position].image?.trim())){
                    val options: RequestOptions =
                        RequestOptions()
                            .centerCrop()
                            .apply(RequestOptions.circleCropTransform())
                            .placeholder(R.drawable.profile_no_image)
                            .priority(Priority.HIGH)
                    Glide
                        .with(context)
                        .load(BaseMediaUrls.USERIMAGE.url + result[local_position].image)
                        .apply(options)
                        .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(imgReviewProfilePhoto)
                }

                if (result[local_position].reviewBy!=null && !TextUtils.isEmpty(result[local_position].reviewBy?.trim())){
                    tvTitle.text = result[local_position].reviewBy
                }
                if (result[local_position].review!=null && !TextUtils.isEmpty(result[local_position].review?.trim())){
                    tvReviewDescription.text = result[local_position].review
                }
            }


        }
    }

}