package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.doctor.myschedule.homeVisitList.Result
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorMyScheduleRecyclerviewBinding
import kotlinx.android.synthetic.main.item_doctor_my_schedule_recyclerview.view.*
import java.util.*

class AdapterDoctorHomeVisitRecyclerView(internal var context: Context) :
    RecyclerView.Adapter<AdapterDoctorHomeVisitRecyclerView.ViewHolder>() {

    companion object {
        val TAG: String = AdapterDoctorHomeVisitRecyclerView::class.java.simpleName
    }

    internal lateinit var recyclerViewItemClickWithView: OnItemClickWithReportIdListener
    var result: LinkedList<Result> = LinkedList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorMyScheduleRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctor_my_schedule_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorMyScheduleRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        init {
            itemView.root.txtViewSchedule?.setOnClickListener {
                recyclerViewItemClickWithView.onItemClick(localPosition)
            }

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            localPosition = pos


            with(itemView) {
                if (result[localPosition].day != null) {
                    txtViewSchedule.text = result[localPosition].day
                }
            }

        }
    }

    fun filterList(dataList: LinkedList<Result>) {
        result = dataList
        notifyDataSetChanged()
    }
}