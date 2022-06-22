package com.rootscare.serviceprovider.ui.caregiver.caregiverupdatepaymenthistory.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.doctor.payment.ResultItem
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorPaymenthistoryRecyclerviewBinding
import kotlinx.android.synthetic.main.item_doctor_paymenthistory_recyclerview.view.*
import java.util.*

class AdapterCaregiverUpdatePaymentHistoryRecyclerview(internal var context: Context) :
    RecyclerView.Adapter<AdapterCaregiverUpdatePaymentHistoryRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterCaregiverUpdatePaymentHistoryRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener
    var result: LinkedList<ResultItem> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorPaymenthistoryRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctor_paymenthistory_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorPaymenthistoryRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

//        init {
//            itemView?.root?.crdview_appoitment_list?.setOnClickListener(View.OnClickListener {
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


//        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            localPosition = pos
            if (localPosition == 0) {
                itemView.rootView?.ll_header?.visibility = View.VISIBLE
                itemView.rootView?.view_header?.visibility = View.VISIBLE
            } else {
                itemView.rootView?.ll_header?.visibility = View.GONE
                itemView.rootView?.view_header?.visibility = View.GONE
            }

            with(itemView) {
                if (result[localPosition].orderId != null) {
                    tvIdValue.setText(result[localPosition].orderId)
                }
                if (result[localPosition].date != null) {
                    tvDateValue.setText(result[localPosition].date)
                }
                if (result[localPosition].amount != null) {
                    tvAmountValue.setText(result[localPosition].amount)
                }
                if (result[localPosition].paymentType != null) {
                    tvPaymentTypeValue.setText(result[localPosition].paymentType)
                }
                if (result[localPosition].paymentStatus != null) {
                    tvPaymentStatusValue.setText(result[localPosition].paymentStatus)
                }
            }

//            itemView?.rootView?.txt_teacher_name?.text= trainerList?.get(pos)?.name
//            itemView?.rootView?.txt_teacher_qualification?.text= "Qualification : "+" "+trainerList?.get(pos)?.qualification
//            if(trainerList?.get(pos)?.avgRating!=null && !trainerList?.get(pos)?.avgRating.equals("")){
//                itemView?.rootView?.ratingBarteacher?.rating= trainerList?.get(pos)?.avgRating?.toFloat()!!
//            }


//            itemView?.rootView?.txt_rating_count?.text="("+contactListItem?.get(pos)?.contactRating+")"
//            (contactListItem?.get(pos)?.contactRating)?.toFloat()?.let { itemView?.rootView?.ratingBar?.setRating(it) }
////            itemView?.rootView?.ratingBar?.rating=1.5f


        }
    }

}