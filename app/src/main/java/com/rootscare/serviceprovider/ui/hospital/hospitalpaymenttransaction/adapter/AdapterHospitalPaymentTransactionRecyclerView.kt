package com.rootscare.serviceprovider.ui.hospital.hospitalpaymenttransaction.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.hospital.ResultItemPayment
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemHospitalPaymentHistoryRecyclerviewBinding
import kotlinx.android.synthetic.main.item_doctor_paymenthistory_recyclerview.view.ll_header
import kotlinx.android.synthetic.main.item_doctor_paymenthistory_recyclerview.view.view_header
import kotlinx.android.synthetic.main.item_hospital_payment_history_recyclerview.view.*

class AdapterHospitalPaymentTransactionRecyclerView(internal var todaysAppointList: ArrayList<ResultItemPayment?>?, internal var context: Context) : RecyclerView.Adapter<AdapterHospitalPaymentTransactionRecyclerView.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalPaymentTransactionRecyclerView::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemHospitalPaymentHistoryRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_hospital_payment_history_recyclerview, parent, false)
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return todaysAppointList!!.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemHospitalPaymentHistoryRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var local_position:Int = 0
        init {
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


        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            if(local_position==0){
                itemView?.rootView?.ll_header?.visibility= View.VISIBLE
                itemView?.rootView?.view_header?.visibility= View.VISIBLE
            }else{
                itemView?.rootView?.ll_header?.visibility= View.GONE
                itemView?.rootView?.view_header?.visibility= View.GONE
            }

            itemView?.rootView?.trsanction_id?.text= todaysAppointList?.get(pos)?.transaction_id
            itemView?.rootView?.date?.text= todaysAppointList?.get(pos)?.date
            itemView?.rootView?.amount?.text= todaysAppointList?.get(pos)?.amount
            itemView?.rootView?.amount?.text= todaysAppointList?.get(pos)?.amount
            itemView?.rootView?.payment_type?.text= todaysAppointList?.get(pos)?.payment_type
            itemView?.rootView?.payment_status?.text= todaysAppointList?.get(pos)?.payment_type

            if(todaysAppointList?.get(pos)?.payment_status.equals("1")){
                itemView?.rootView?.payment_status?.text="Approved"
            }
            else{
                itemView?.rootView?.payment_status?.text="Pending"
            }
//            if(trainerList?.get(pos)?.avgRating!=null && !trainerList?.get(pos)?.avgRating.equals("")){
//                itemView?.rootView?.ratingBarteacher?.rating= trainerList?.get(pos)?.avgRating?.toFloat()!!
//            }





//            itemView?.rootView?.txt_rating_count?.text="("+contactListItem?.get(pos)?.contactRating+")"
//            (contactListItem?.get(pos)?.contactRating)?.toFloat()?.let { itemView?.rootView?.ratingBar?.setRating(it) }
////            itemView?.rootView?.ratingBar?.rating=1.5f


        }
    }

}