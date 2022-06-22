package com.rootscare.serviceprovider.ui.hospital.hospitalmanageappointments.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.doctor.appointment.pastappointment.ResultItem
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemHospitalPastAppointmentRecyclerviewBinding
import kotlinx.android.synthetic.main.item_hospital_past_appointment_recyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AdapterHospitalPastAppointmentRecyclerview(
    val upcomingAppointmentList: LinkedList<ResultItem>?,
    internal var context: Context,
    var isDoctor: Boolean
) : RecyclerView.Adapter<AdapterHospitalPastAppointmentRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterHospitalPastAppointmentRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener
    var startTime = ""
    var endTime = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemHospitalPastAppointmentRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_hospital_past_appointment_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return upcomingAppointmentList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemHospitalPastAppointmentRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            localPosition = pos

            if (upcomingAppointmentList?.get(pos)?.orderId != null
                && !upcomingAppointmentList[pos].orderId.equals("")
            ) {
                itemView.rootView?.order_id?.text = upcomingAppointmentList[pos].orderId
            } else {
                itemView.rootView?.order_id?.text = ""
            }

            if (upcomingAppointmentList?.get(pos)?.patientName != null && !upcomingAppointmentList[pos].patientName.equals("")
            ) {
                itemView.rootView?.txt_patient_name?.text = upcomingAppointmentList[pos].patientName
            } else {
                itemView.rootView?.txt_patient_name?.text = ""
            }
            if (upcomingAppointmentList?.get(pos)?.bookingDate != null
                && !upcomingAppointmentList[pos].bookingDate.equals("")
            ) {
                itemView.rootView?.txt_booking_date?.text = formatDateFromString(
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
            if (isDoctor) {
                itemView.rootView?.tv_appointment?.text = "Appointment Time : "
                itemView.rootView?.txt_time?.text = "$startTime-$endTime"
            } else {
                itemView.rootView?.tv_appointment?.text = "Test Name : "
                itemView.rootView?.txt_time?.text = upcomingAppointmentList?.get(pos)?.pathologyName
            }
            if (upcomingAppointmentList?.get(pos)?.appointmentDate != null
                && !upcomingAppointmentList[pos].appointmentDate.equals("")
            ) {
                itemView.rootView?.txt_appointment_date?.text = formatDateFromString(
                    "yyyy-MM-dd",
                    "dd MMM yyyy",
                    upcomingAppointmentList[pos].appointmentDate
                )
            } else {
                itemView.rootView?.txt_appointment_date?.text = ""
            }

        }

        fun formatDateFromString(
            inputFormat: String?,
            outputFormat: String?,
            inputDate: String?
        ): String {
            val parsed: Date
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