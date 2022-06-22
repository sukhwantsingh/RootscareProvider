package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.subfragment.upcomingappointment.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.doctor.appointment.upcomingappointment.ResultItem
import com.rootscare.interfaces.OnClickOfDoctorAppointment2
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorUpcomingAppointmentRecyclerviewBinding
import kotlinx.android.synthetic.main.item_doctor_upcoming_appointment_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AdapterNurseUpcommingAppointment(val upcomingAppointmentList: ArrayList<ResultItem?>?, internal var context: Context) :
    RecyclerView.Adapter<AdapterNurseUpcommingAppointment.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterNurseUpcommingAppointment::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnClickOfDoctorAppointment2
    var startTime = ""
    var endTime = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding =
            DataBindingUtil.inflate<ItemDoctorUpcomingAppointmentRecyclerviewBinding>(
                LayoutInflater.from(context),
                R.layout.item_doctor_upcoming_appointment_recyclerview, parent, false
            )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return upcomingAppointmentList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorUpcomingAppointmentRecyclerviewBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        private var local_position: Int = 0

        init {
            itemView.root.btnViewDetails?.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView.onItemClick(local_position)
            })
            itemView.btnReject.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView.onRejectBtnBtnClick(local_position.toString(), "Reject")
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
            local_position = pos

            if (upcomingAppointmentList?.get(pos)?.orderId != null && !upcomingAppointmentList.get(pos)?.orderId.equals("")) {
                itemView.rootView?.txt_appointment?.text = upcomingAppointmentList.get(pos)?.orderId
            } else {
                itemView.rootView?.txt_appointment?.text = ""
            }

            if (upcomingAppointmentList?.get(pos)?.patientName != null && !upcomingAppointmentList.get(pos)?.patientName.equals("")) {
                itemView.rootView?.txt_upcoming_patient_name?.text = upcomingAppointmentList.get(pos)?.patientName
            } else {
                itemView.rootView?.txt_upcoming_patient_name?.text = ""
            }
            if (upcomingAppointmentList?.get(pos)?.bookingDate != null && !upcomingAppointmentList[pos]?.bookingDate.equals("")) {
                itemView.rootView?.txt_upcoming_booking_date?.text = formatDateFromString(
                    "yyyy-MM-dd", "dd MMM yyyy",
                    upcomingAppointmentList[pos]?.bookingDate
                )
            } else {
                itemView.rootView?.txt_upcoming_booking_date?.text = ""
            }
            startTime = if (upcomingAppointmentList?.get(pos)?.fromTime != null && !upcomingAppointmentList[pos]?.fromTime.equals("")) {
                upcomingAppointmentList[pos]?.fromTime!!
            } else {
                ""
            }

            endTime = if (upcomingAppointmentList?.get(pos)?.toTime != null && !upcomingAppointmentList.get(pos)?.toTime.equals("")) {
                upcomingAppointmentList.get(pos)?.toTime!!
            } else {
                ""
            }

            itemView.rootView?.txt_upcoming_time?.text = "$startTime-$endTime"
            if (upcomingAppointmentList?.get(pos)?.fromDate != null && !upcomingAppointmentList[pos]?.fromDate.equals("")) {
                itemView.rootView?.txt_upcoming_appointment_date?.text =
                    formatDateFromString("yyyy-MM-dd", "dd MMM yyyy", upcomingAppointmentList[pos]?.fromDate)
            } else {
                itemView.rootView?.txt_upcoming_appointment_date?.text = ""
            }

            if (upcomingAppointmentList?.get(pos)?.acceptanceStatus != null && !TextUtils.isEmpty(upcomingAppointmentList[pos]?.acceptanceStatus?.trim()) &&
                upcomingAppointmentList[pos]?.acceptanceStatus?.lowercase(Locale.ROOT)?.contains("reject")!!
            ) {
                itemView.rootView?.btn_reject?.visibility = View.GONE
//            } else {
//                itemView.rootView?.btn_reject?.visibility = View.VISIBLE
            }


//            itemView?.rootView?.txt_teacher_name?.text= trainerList?.get(pos)?.name
//            itemView?.rootView?.txt_teacher_qualification?.text= "Qualification : "+" "+trainerList?.get(pos)?.qualification
//            if(trainerList?.get(pos)?.avgRating!=null && !trainerList?.get(pos)?.avgRating.equals("")){
//                itemView?.rootView?.ratingBarteacher?.rating= trainerList?.get(pos)?.avgRating?.toFloat()!!
//            }


//            itemView?.rootView?.txt_rating_count?.text="("+contactListItem?.get(pos)?.contactRating+")"
//            (contactListItem?.get(pos)?.contactRating)?.toFloat()?.let { itemView?.rootView?.ratingBar?.setRating(it) }
////            itemView?.rootView?.ratingBar?.rating=1.5f

            if (upcomingAppointmentList?.get(local_position)?.appointmentStatus != null) {
                if (upcomingAppointmentList.get(local_position)?.appointmentStatus?.lowercase(Locale.ROOT)?.contains("complete")!!) {
                    itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Completed"
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                        .setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                } else if (upcomingAppointmentList.get(local_position)?.appointmentStatus?.lowercase(Locale.ROOT)?.contains("reject")!!) {
                    itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Rejected"
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                        .setTextColor(ContextCompat.getColor(context, R.color.red))
                } else if (upcomingAppointmentList.get(local_position)?.appointmentStatus?.lowercase(Locale.ROOT)?.contains("cancel")!!) {
                    itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Cancelled"
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                        .setTextColor(ContextCompat.getColor(context, R.color.red))
                } else if (upcomingAppointmentList.get(local_position)?.appointmentStatus?.lowercase(Locale.ROOT)?.contains("book")!!) {
                    itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Booked"
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                        .setTextColor(ContextCompat.getColor(context, R.color.orange))
                } else {
                    itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.GONE
                }
            } else {
                itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.GONE
            }

        }

        fun formatDateFromString(inputFormat: String?, outputFormat: String?, inputDate: String?): String? {
            var parsed: Date? = null
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
