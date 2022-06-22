package com.rootscare.serviceprovider.ui.caregiver.caregivermyappointment.subfragment.newappointment.adapter

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
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.ResultItem
import com.rootscare.interfaces.OnClickOfDoctorAppointment
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorRequestedAppointmentlistBinding
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.adapter.AdapterDoctorTodaysAppointmentRecyclerview
import kotlinx.android.synthetic.main.item_doctor_requested_appointmentlist.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterRequestedAppointmentListForCaregiver(val requestedappointmentList: ArrayList<ResultItem?>?, internal var context: Context) :
    RecyclerView.Adapter<AdapterRequestedAppointmentListForCaregiver.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterDoctorTodaysAppointmentRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnClickOfDoctorAppointment
    var startTime = ""
    var endTime = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding =
            DataBindingUtil.inflate<ItemDoctorRequestedAppointmentlistBinding>(
                LayoutInflater.from(context),
                R.layout.item_doctor_requested_appointmentlist, parent, false
            )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return requestedappointmentList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemDoctorRequestedAppointmentlistBinding) :
        RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        init {
            itemView.btnViewDetails.setOnClickListener {
                recyclerViewItemClickWithView.onItemClick(localPosition)
            }
            itemView.root.btn_accept?.setOnClickListener {
                recyclerViewItemClickWithView.onAcceptBtnClick(localPosition.toString(), "Accept")
            }

            itemView.btnRejecttt.setOnClickListener {
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

            if (requestedappointmentList?.get(pos)?.orderId != null && !requestedappointmentList[pos]?.orderId.equals("")) {
                itemView.rootView?.txt_requested_appointment?.text = requestedappointmentList[pos]?.orderId
            } else {
                itemView.rootView?.txt_requested_appointment?.text = ""
            }

            if (requestedappointmentList?.get(pos)?.patientName != null && !requestedappointmentList[pos]?.patientName.equals("")) {
                itemView.rootView?.txt_requested_patient_name?.text = requestedappointmentList[pos]?.patientName
            } else {
                itemView.rootView?.txt_requested_patient_name?.text = ""
            }
            if (requestedappointmentList?.get(pos)?.bookingDate != null && !requestedappointmentList[pos]?.bookingDate.equals("")) {
                itemView.rootView?.txt_requested_booking_date?.text = formatDateFromString(
                    "yyyy-MM-dd", "dd MMM yyyy",
                    requestedappointmentList[pos]?.bookingDate
                )
            } else {
                itemView.rootView?.txt_requested_booking_date?.text = ""
            }
            startTime = if (requestedappointmentList?.get(pos)?.fromTime != null && !requestedappointmentList[pos]?.fromTime.equals("")) {
                requestedappointmentList[pos]?.fromTime!!
            } else {
                ""
            }

            endTime = if (requestedappointmentList?.get(pos)?.toTime != null && !requestedappointmentList[pos]?.toTime.equals("")) {
                requestedappointmentList[pos]?.toTime!!
            } else {
                ""
            }

            itemView.rootView?.txt_requested_time?.text = "$startTime-$endTime"
            if (requestedappointmentList?.get(pos)?.fromDate != null && !requestedappointmentList[pos]?.fromDate.equals("")) {
                itemView.rootView?.txt_requested_appointment_date?.text = formatDateFromString(
                    "yyyy-MM-dd", "dd MMM yyyy",
                    requestedappointmentList[pos]?.fromDate
                )
            } else {
                itemView.rootView?.txt_requested_appointment_date?.text = ""
            }


            if (requestedappointmentList?.get(pos)?.acceptanceStatus != null && !TextUtils.isEmpty(requestedappointmentList[pos]?.acceptanceStatus?.trim())) {
                when {
                    requestedappointmentList[pos]?.acceptanceStatus?.lowercase(Locale.ROOT)?.contains("reject")!! -> {
                        itemView.rootView?.btn_rejecttt?.visibility = View.GONE
                        itemView.rootView?.btn_accept?.visibility = View.GONE
                    }
                    requestedappointmentList[pos]?.acceptanceStatus?.lowercase(Locale.ROOT)?.contains("accept")!! -> {
                        itemView.rootView?.btn_rejecttt?.visibility = View.GONE
                        itemView.rootView?.btn_accept?.visibility = View.GONE
                    }
                    else -> {
                        itemView.rootView?.btn_rejecttt?.visibility = View.VISIBLE
                        itemView.rootView?.btn_accept?.visibility = View.VISIBLE
                    }
                }
            }


            if (requestedappointmentList?.get(localPosition)?.appointmentStatus != null) {
                when {
                    requestedappointmentList[localPosition]?.appointmentStatus?.lowercase(Locale.ROOT)?.contains("complete")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Completed"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                    }
                    requestedappointmentList[localPosition]?.appointmentStatus?.lowercase(Locale.ROOT)?.contains("reject")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Rejected"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.red))
                    }
                    requestedappointmentList[localPosition]?.appointmentStatus?.lowercase(Locale.ROOT)?.contains("cancel")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Cancelled"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.red))
                    }
                    requestedappointmentList[localPosition]?.appointmentStatus?.lowercase(Locale.ROOT)?.contains("book")!! -> {
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

        private fun formatDateFromString(inputFormat: String?, outputFormat: String?, inputDate: String?):
                String {
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
