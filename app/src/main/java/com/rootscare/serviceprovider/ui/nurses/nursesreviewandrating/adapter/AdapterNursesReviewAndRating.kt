package com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.doctor.review.ResultItem

import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutItemNewRatingReviewBinding
import com.rootscare.serviceprovider.utilitycommon.setCircularRemoteImage
import java.util.*
interface OnReviewClickCallback{
    fun onReviewClick(reviewText:String)
}
class AdapterNursesReviewAndRating(internal var context: Context) : RecyclerView.Adapter<AdapterNursesReviewAndRating.ViewHolder>() {

   internal lateinit var mCallback: OnReviewClickCallback

    var result: LinkedList<ResultItem> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<LayoutItemNewRatingReviewBinding>(
            LayoutInflater.from(context),
       //     R.layout.item_review_and_rating_recyclerview, parent, false)
            R.layout.layout_item_new_rating_review, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindData(result.get(position))
    }


    inner class ViewHolder(var binding: LayoutItemNewRatingReviewBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cnsRoot.setOnClickListener {
                mCallback.onReviewClick(result.get(absoluteAdapterPosition).review?:"")
            }
        }

        fun onBindData(mData: ResultItem) {
            with(binding) {
                try {
                    tvReviewText.text = mData.review ?: "No review"
                    tvId.text = mData.orderId
                    ratBar.rating = if(mData.rating.isNullOrBlank()) "0".toFloat() else mData.rating.trim().toFloat()
                    ivUser.setCircularRemoteImage(mData.image)
                } catch (e:Exception){
                   println(e)
                }
            }
        }
    }

}