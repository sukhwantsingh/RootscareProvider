package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.adapter

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
import com.rootscare.data.model.response.doctor.appointment.todaysappointment.ResultItem
import com.rootscare.interfaces.OnClickOfDoctorAppointment2
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorTodaysAppointmentrecyclerviewBinding
import kotlinx.android.synthetic.main.item_doctor_todays_appointmentrecyclerview.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterDoctorTodaysAppointmentRecyclerview(internal var todaysAppointList: ArrayList<ResultItem?>?, internal var context: Context) :
    RecyclerView.Adapter<AdapterDoctorTodaysAppointmentRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterDoctorTodaysAppointmentRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//

    internal lateinit var recyclerViewItemClickWithView2: OnClickOfDoctorAppointment2
    var startTime = ""
    var endTime = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding =
            DataBindingUtil.inflate<ItemDoctorTodaysAppointmentrecyclerviewBinding>(
                LayoutInflater.from(context),
                R.layout.item_doctor_todays_appointmentrecyclerview, parent, false
            )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return todaysAppointList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(internal var itemVie: ItemDoctorTodaysAppointmentrecyclerviewBinding) :
        RecyclerView.ViewHolder(itemVie.root) {

        private var localPosition: Int = 0

        init {
            itemVie.btnViewDetails.setOnClickListener {
                recyclerViewItemClickWithView2.onItemClick(localPosition)
            }
            itemVie.btnCompleted.setOnClickListener {
                recyclerViewItemClickWithView2.onAcceptBtnClick(localPosition.toString(), "")
            }
            itemVie.btnReject.setOnClickListener {
                recyclerViewItemClickWithView2.onRejectBtnBtnClick(localPosition.toString(), "Reject")
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

            if (todaysAppointList?.get(pos)?.orderId != null && !todaysAppointList?.get(pos)?.orderId.equals("")) {
                itemView.rootView?.txt_todays_appointment?.text = todaysAppointList?.get(pos)?.orderId
            } else {
                itemView.rootView?.txt_todays_appointment?.text = ""
            }

            if (todaysAppointList?.get(pos)?.patientName != null && !todaysAppointList?.get(pos)?.patientName.equals("")) {
                itemView.rootView?.txt_todays_patient_name?.text = todaysAppointList?.get(pos)?.patientName
            } else {
                itemView.rootView?.txt_todays_patient_name?.text = ""
            }
            if (todaysAppointList?.get(pos)?.bookingDate != null && !todaysAppointList?.get(pos)?.bookingDate.equals("")) {
                itemView.rootView?.txt_todays_booking_date?.text =
                    formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", todaysAppointList?.get(pos)?.bookingDate)
            } else {
                itemView.rootView?.txt_todays_booking_date?.text = ""
            }
            startTime = if (todaysAppointList?.get(pos)?.fromTime != null && !todaysAppointList?.get(pos)?.fromTime.equals("")) {
                todaysAppointList?.get(pos)?.fromTime!!
            } else {
                ""
            }

            endTime = if (todaysAppointList?.get(pos)?.toTime != null && !todaysAppointList?.get(pos)?.toTime.equals("")) {
                todaysAppointList?.get(pos)?.toTime!!
            } else {
                ""
            }

            itemView.rootView?.txt_todays_time?.text = "$startTime-$endTime"
            if (todaysAppointList?.get(pos)?.appointmentDate != null && !todaysAppointList?.get(pos)?.appointmentDate.equals("")) {
                itemView.rootView?.txt_todays_appointment_date?.text =
                    formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", todaysAppointList?.get(pos)?.appointmentDate)
            } else {
                itemView.rootView?.txt_todays_appointment_date?.text = ""
            }


            if (todaysAppointList?.get(pos)?.isCompletedButtonVisible!!) {
                itemView.btnCompleted.visibility = View.VISIBLE
                if (todaysAppointList?.get(pos)?.uploadPrescription != null && !TextUtils.isEmpty(todaysAppointList?.get(pos)?.uploadPrescription?.trim())) {
//                itemView.btnCompleted.visibility = View.GONE
                } else {
                    if (todaysAppointList?.get(pos)?.appointmentStatus != null && !TextUtils.isEmpty(todaysAppointList?.get(pos)?.appointmentStatus?.trim()) &&
                        todaysAppointList?.get(pos)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("completed")!!
                    ) {
//                        itemVie.llMainlayout.background = ContextCompat.getDrawable(context, R.drawable.background_green_stroke_box)
                        itemView.btnCompleted.visibility = View.VISIBLE
                        itemView.btn_reject.visibility = View.GONE
                        itemView.btnCompleted.text = "Upload Prescription"
                        itemView.btnCompleted.setOnClickListener {
                            recyclerViewItemClickWithView2.onUploadBtnClick(localPosition.toString(), "")
                        }
                    } else {
//                        itemVie.llMainlayout.background = null
                        itemView.btnCompleted.visibility = View.VISIBLE
                        itemView.btnCompleted.text = "Complete"
                        itemView.btnCompleted.setOnClickListener {
                            recyclerViewItemClickWithView2.onAcceptBtnClick(localPosition.toString(), "")
                        }
                        if (todaysAppointList?.get(pos)?.acceptanceStatus != null && !TextUtils.isEmpty(todaysAppointList?.get(pos)?.acceptanceStatus?.trim()) &&
                            todaysAppointList?.get(pos)?.acceptanceStatus?.toLowerCase(Locale.ROOT)?.contains("reject")!!
                        ) {
                            itemView.btn_reject.visibility = View.GONE
                        } else {
//                            itemView.btn_reject.visibility = View.VISIBLE
                        }
                    }
                }
            } else {
                itemView.btnCompleted.visibility = View.GONE
            }


            if (todaysAppointList?.get(localPosition)?.appointmentStatus != null) {
                when {
                    todaysAppointList?.get(localPosition)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("complete")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Completed"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                    }
                    todaysAppointList?.get(localPosition)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("reject")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Rejected"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.red))
                    }
                    todaysAppointList?.get(localPosition)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("cancel")!! -> {
                        itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).text = "Cancelled"
                        itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                            .setTextColor(ContextCompat.getColor(context, R.color.red))
                    }
                    todaysAppointList?.get(localPosition)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("book")!! -> {
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

        fun formateDateFromstring(inputFormat: String?, outputFormat: String?, inputDate: String?): String? {
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
