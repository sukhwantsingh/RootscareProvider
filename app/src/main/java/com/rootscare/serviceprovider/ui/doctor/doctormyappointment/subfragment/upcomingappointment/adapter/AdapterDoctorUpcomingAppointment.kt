package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.adapter

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
import kotlinx.android.synthetic.main.item_doctor_upcoming_appointment_recyclerview.view.btnViewDetails
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterDoctorUpcomingAppointment(val upcomingAppointmentList: ArrayList<ResultItem?>?, internal var context: Context) :
    RecyclerView.Adapter<AdapterDoctorUpcomingAppointment.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterDoctorUpcomingAppointment::class.java.simpleName
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

        private var localPosition: Int = 0

        init {
            itemView.root.btnViewDetails?.setOnClickListener {
                recyclerViewItemClickWithView.onItemClick(localPosition)
            }
            itemView.btnReject.setOnClickListener {
                recyclerViewItemClickWithView.onRejectBtnBtnClick(localPosition.toString(), "Reject")
            }
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

            if (upcomingAppointmentList?.get(pos)?.orderId != null && !upcomingAppointmentList[pos]?.orderId.equals("")) {
                itemView.rootView?.txt_appointment?.text = upcomingAppointmentList[pos]?.orderId
            } else {
                itemView.rootView?.txt_appointment?.text = ""
            }

            if (upcomingAppointmentList?.get(pos)?.patientName != null && !upcomingAppointmentList[pos]?.patientName.equals("")) {
                itemView.rootView?.txt_upcoming_patient_name?.text = upcomingAppointmentList[pos]?.patientName
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

            endTime = if (upcomingAppointmentList?.get(pos)?.toTime != null && !upcomingAppointmentList[pos]?.toTime.equals("")) {
                upcomingAppointmentList[pos]?.toTime!!
            } else {
                ""
            }

            itemView.rootView?.txt_upcoming_time?.text = "$startTime-$endTime"
            if (upcomingAppointmentList?.get(pos)?.appointmentDate != null && !upcomingAppointmentList[pos]?.appointmentDate.equals("")) {
                itemView.rootView?.txt_upcoming_appointment_date?.text = formatDateFromString(
                    "yyyy-MM-dd", "dd MMM yyyy",
                    upcomingAppointmentList[pos]?.appointmentDate
                )
            } else {
                itemView.rootView?.txt_upcoming_appointment_date?.text = ""
            }

            if (upcomingAppointmentList?.get(pos)?.acceptanceStatus != null && !TextUtils.isEmpty(upcomingAppointmentList[pos]?.acceptanceStatus?.trim()) &&
                upcomingAppointmentList[pos]?.acceptanceStatus?.toLowerCase(Locale.ROOT)?.contains("reject")!!
            ) {
                itemView.rootView?.btn_reject?.visibility = View.GONE
//            } else {
//                itemView?.rootView?.btn_rejectt?.visibility = View.VISIBLE
            }


            if (upcomingAppointmentList?.get(localPosition)?.appointmentStatus != null) {
                when {
                    upcomingAppointmentList[localPosition]?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("complete")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Completed"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                    }
                    upcomingAppointmentList[localPosition]?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("reject")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Rejected"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.red))
                    }
                    upcomingAppointmentList[localPosition]?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("cancel")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Cancelled"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.red))
                    }
                    upcomingAppointmentList[localPosition]?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("book")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Booked"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.orange))
                    }
                    else -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.GONE
                    }
                }
            } else {
                itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.GONE
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

        private fun formatDateFromString(
            inputFormat: String?, outputFormat: String?,
            inputDate: String?
        ): String {
            val parsed: Date?
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
