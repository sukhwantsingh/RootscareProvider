package com.rootscare.serviceprovider.ui.doctor.doctormyschedule.subfragment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.doctor.myschedule.homeVisitList.Slot
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorViewScheduleListBinding
import kotlinx.android.synthetic.main.item_doctor_view_schedule_list.view.*
import java.util.*

class AdapterDoctorHomeVisitRecyclerview(internal var context: Context) :
    RecyclerView.Adapter<AdapterDoctorHomeVisitRecyclerview.ViewHolder>() {
    companion object {
        val TAG: String = AdapterDoctorHomeVisitRecyclerview::class.java.simpleName
    }

    internal lateinit var recyclerViewItemClickWithView: OnItemClickWithReportIdListener
    var result: LinkedList<Slot> = LinkedList()
    var day: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorViewScheduleListBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctor_view_schedule_list, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return result.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorViewScheduleListBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        init {
            itemView.root.btn_remove?.setOnClickListener {
                recyclerViewItemClickWithView.onItemClick(localPosition)
            }

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            println()
            localPosition = pos
            with(itemView) {
                if (day != null) {
                    tvSlotName.text = day
                }
                if (result[localPosition].timeFrom != null) {
                    tvStartTime.text = result[localPosition].timeFrom
                }
                if (result[localPosition].timeTo != null) {
                    tvEndTime.text = result[localPosition].timeTo
                }
            }
        }
    }

}