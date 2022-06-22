package com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.pastappointment.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResultItem
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorPastAppointmentlistBinding
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.adapter.AdapterDoctorTodaysAppointmentRecyclerview
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.upcomingappointment.adapter.AdapterDoctorUpcomingAppointment
import kotlinx.android.synthetic.main.item_doctor_upcoming_appointment_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AdapterPastAppointmentListForCaregiver(
    val upcomingAppointmentList: LinkedList<ResultItem>?,
    internal var context: Context
) : RecyclerView.Adapter<AdapterPastAppointmentListForCaregiver.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterDoctorTodaysAppointmentRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal var recyclerViewItemClickWithView: OnItemClikWithIdListener? = null
    var startTime = ""
    var endTime = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding =
            DataBindingUtil.inflate<ItemDoctorPastAppointmentlistBinding>(
                LayoutInflater.from(context),
                R.layout.item_doctor_past_appointmentlist, parent, false
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


    inner class ViewHolder(itemView: ItemDoctorPastAppointmentlistBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        init {

            itemView.btnViewDetails.setOnClickListener {
                if (upcomingAppointmentList?.get(localPosition)?.id?.toInt() != null && recyclerViewItemClickWithView != null) {
                    recyclerViewItemClickWithView?.onItemClick(
                        localPosition
                    )
                }
            }
        }

        fun onBind(pos: Int) {
            Log.d(AdapterDoctorUpcomingAppointment.TAG, " true")
            localPosition = pos

            if (upcomingAppointmentList?.get(pos)?.orderId != null && !upcomingAppointmentList?.get(
                    pos
                ).orderId.equals("")
            ) {
                itemView.rootView?.txt_appointment?.text = upcomingAppointmentList.get(pos).orderId
            } else {
                itemView.rootView?.txt_appointment?.text = ""
            }

            if (upcomingAppointmentList?.get(pos)?.patientName != null && !upcomingAppointmentList?.get(
                    pos
                ).patientName.equals("")
            ) {
                itemView.rootView?.txt_upcoming_patient_name?.text = upcomingAppointmentList?.get(
                    pos
                ).patientName
            } else {
                itemView.rootView?.txt_upcoming_patient_name?.text = ""
            }
            if (upcomingAppointmentList?.get(pos)?.bookingDate != null && !upcomingAppointmentList?.get(
                    pos
                ).bookingDate.equals("")
            ) {
                itemView.rootView?.txt_upcoming_booking_date?.text = formatDateFromString(
                    "yyyy-MM-dd",
                    "dd MMM yyyy",
                    upcomingAppointmentList.get(pos).bookingDate
                )
            } else {
                itemView.rootView?.txt_upcoming_booking_date?.text = ""
            }
            startTime = if (upcomingAppointmentList?.get(pos)?.fromTime != null && !upcomingAppointmentList?.get(
                    pos
                ).fromTime.equals("")
            ) {
                upcomingAppointmentList.get(pos).fromTime!!
            } else {
                ""
            }

            endTime = if (upcomingAppointmentList?.get(pos)?.toTime != null && !upcomingAppointmentList?.get(
                    pos
                ).toTime.equals("")
            ) {
                upcomingAppointmentList.get(pos).toTime!!
            } else {
                ""
            }

            itemView.rootView?.txt_upcoming_time?.text = startTime + "-" + endTime
            if (upcomingAppointmentList?.get(pos)?.fromDate != null && !upcomingAppointmentList?.get(
                    pos
                ).fromDate.equals("")
            ) {
                itemView.rootView?.txt_upcoming_appointment_date?.text = formatDateFromString(
                    "yyyy-MM-dd",
                    "dd MMM yyyy",
                    upcomingAppointmentList.get(pos).fromDate
                )
            } else {
                itemView.rootView?.txt_upcoming_appointment_date?.text = ""
            }

            if (upcomingAppointmentList?.get(localPosition)?.appointmentStatus != null) {
                when {
                    upcomingAppointmentList.get(localPosition).appointmentStatus?.lowercase(Locale.ROOT)?.contains("complete")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Completed"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                    }
                    upcomingAppointmentList.get(localPosition).appointmentStatus?.lowercase(Locale.ROOT)?.contains("reject")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Rejected"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.red))
                    }
                    upcomingAppointmentList.get(localPosition).appointmentStatus?.lowercase(Locale.ROOT)?.contains("cancel")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Cancelled"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.red))
                    }
                    upcomingAppointmentList.get(localPosition).appointmentStatus?.lowercase(Locale.ROOT)?.contains("book")!! -> {
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


        }

        fun formatDateFromString(
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
