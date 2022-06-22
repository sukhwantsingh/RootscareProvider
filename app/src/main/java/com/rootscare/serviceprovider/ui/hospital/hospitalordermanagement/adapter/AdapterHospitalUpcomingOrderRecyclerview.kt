package com.rootscare.serviceprovider.ui.hospital.hospitalordermanagement.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.hospital.ResultItemLab
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemHospitalUpcomingOrderRecyclerviewBinding
import kotlinx.android.synthetic.main.item_hospital_past_appointment_recyclerview.view.*
import kotlinx.android.synthetic.main.item_hospital_upcoming_order_recyclerview.view.*
import kotlinx.android.synthetic.main.item_hospital_upcoming_order_recyclerview.view.txt_appointment_date
import kotlinx.android.synthetic.main.item_hospital_upcoming_order_recyclerview.view.txt_booking_date
import kotlinx.android.synthetic.main.item_hospital_upcoming_order_recyclerview.view.txt_patient_name
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AdapterHospitalUpcomingOrderRecyclerview(
    val upcomingAppointmentList: LinkedList<ResultItemLab>?,
    internal var context: Context
) : RecyclerView.Adapter<AdapterHospitalUpcomingOrderRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalUpcomingOrderRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClickWithReportIdListener
    var startTime = ""
    var endTime = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemHospitalUpcomingOrderRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_hospital_upcoming_order_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return upcomingAppointmentList!!.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemHospitalUpcomingOrderRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        init {
            itemView.root.crdview_hospitalupcomingorder_list?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView.onItemClick(1)
            })
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
            localPosition = pos

            if (upcomingAppointmentList?.get(pos)?.order_id != null && !upcomingAppointmentList[pos].order_id.equals("")
            ) {
                itemView.rootView?.order_id?.text = upcomingAppointmentList[pos].order_id
            } else {
                itemView.rootView?.order_id?.text = ""
            }

            if (upcomingAppointmentList?.get(pos)?.patientName != null
                && !upcomingAppointmentList[pos].patientName.equals("")
            ) {
                itemView.rootView?.txt_patient_name?.text = upcomingAppointmentList[pos].patientName
            } else {
                itemView.rootView?.txt_patient_name?.text = ""
            }
            if (upcomingAppointmentList?.get(pos)?.bookingDate != null
                && !upcomingAppointmentList[pos].bookingDate.equals("")
            ) {
                itemView.rootView?.txt_booking_date?.text = formateDateFromstring(
                    "yyyy-MM-dd",
                    "dd MMM yyyy",
                    upcomingAppointmentList[pos].bookingDate
                )
            } else {
                itemView.rootView?.txt_booking_date?.text = ""
            }
            startTime = if (upcomingAppointmentList?.get(pos)?.fromTime != null
                && !upcomingAppointmentList[pos].fromTime.equals("")
            ) {
                upcomingAppointmentList[pos].fromTime!!
            } else {
                ""
            }

            endTime = if (upcomingAppointmentList?.get(pos)?.toTime != null
                && !upcomingAppointmentList[pos].toTime.equals("")
            ) {
                upcomingAppointmentList[pos].toTime!!
            } else {
                ""
            }

            itemView.rootView?.txt_time?.text = "$startTime-$endTime"
            if (upcomingAppointmentList?.get(pos)?.appointmentDate != null
                && !upcomingAppointmentList[pos].appointmentDate.equals("")
            ) {
                itemView.rootView?.txt_appointment_date?.text = formateDateFromstring(
                    "yyyy-MM-dd",
                    "dd MMM yyyy",
                    upcomingAppointmentList.get(pos).appointmentDate
                )
            } else {
                itemView.rootView?.txt_appointment_date?.text = ""
            }

        }

        fun formateDateFromstring(
            inputFormat: String?,
            outputFormat: String?,
            inputDate: String?
        ): String {
            var parsed: Date?
            var outputDate = ""
            val df_input =
                SimpleDateFormat(inputFormat, Locale.getDefault())
            val df_output =
                SimpleDateFormat(outputFormat, Locale.getDefault())
            try {
                parsed = df_input.parse(inputDate)
                outputDate = df_output.format(parsed)
            } catch (e: ParseException) {
            }
            return outputDate
        }
    }
}

