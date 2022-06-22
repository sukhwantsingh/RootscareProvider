package com.rootscare.serviceprovider.ui.babySitter.babySitterMyAppointment.subfragment.todayappointment.adapter

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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AdapterBabysitterTodaysAppointmentRecyclerview (internal var todaysAppointList: ArrayList<ResultItem?>?, internal var context: Context) : RecyclerView.Adapter<AdapterBabysitterTodaysAppointmentRecyclerview.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterBabysitterTodaysAppointmentRecyclerview::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//

    internal lateinit var recyclerViewItemClickWithView2: OnClickOfDoctorAppointment2
    var startTime=""
    var endTime=""

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

        private var local_position: Int = 0

        init {
            with(itemVie) {
                btnViewDetails.setOnClickListener(View.OnClickListener {
                    recyclerViewItemClickWithView2.onItemClick(local_position)
                })
                btnCompleted.setOnClickListener {
                    recyclerViewItemClickWithView2.onAcceptBtnClick(local_position.toString(), "")
                }
                btnReject.setOnClickListener(View.OnClickListener {
                    recyclerViewItemClickWithView2.onRejectBtnBtnClick(local_position.toString(), "Reject")
                })
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
            local_position = pos

            with(itemVie) {
                if (todaysAppointList?.get(pos)?.orderId != null && !todaysAppointList?.get(pos)?.orderId.equals("")) {
                    txtTodaysAppointment?.text = todaysAppointList?.get(pos)?.orderId
                } else {
                    txtTodaysAppointment?.text = ""
                }

                if (todaysAppointList?.get(pos)?.patientName != null && !todaysAppointList?.get(pos)?.patientName.equals("")) {
                    txtTodaysPatientName?.text = todaysAppointList?.get(pos)?.patientName
                } else {
                    txtTodaysPatientName?.text = ""
                }
                if (todaysAppointList?.get(pos)?.bookingDate != null && !todaysAppointList?.get(pos)?.bookingDate.equals("")) {
                    txtTodaysBookingDate?.text =
                        formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", todaysAppointList?.get(pos)?.bookingDate)
                } else {
                    txtTodaysBookingDate?.text = ""
                }
                if (todaysAppointList?.get(pos)?.fromTime != null && !todaysAppointList?.get(pos)?.fromTime.equals("")) {
                    startTime = todaysAppointList?.get(pos)?.fromTime!!
                } else {
                    startTime = ""
                }

                if (todaysAppointList?.get(pos)?.toTime != null && !todaysAppointList?.get(pos)?.toTime.equals("")) {
                    endTime = todaysAppointList?.get(pos)?.toTime!!
                } else {
                    endTime = ""
                }

                txtTodaysTime.text = startTime + "-" + endTime
                if (todaysAppointList?.get(pos)?.fromDate != null && !todaysAppointList?.get(pos)?.fromDate.equals("")) {
                    txtTodaysAppointmentDate?.text =
                        formateDateFromstring("yyyy-MM-dd", "dd MMM yyyy", todaysAppointList?.get(pos)?.fromDate)
                } else {
                    txtTodaysAppointmentDate?.text = ""
                }

                if (todaysAppointList?.get(pos)?.uploadPrescription != null && !TextUtils.isEmpty(todaysAppointList?.get(pos)?.uploadPrescription?.trim())) {
//                itemView.btnCompleted.visibility = View.GONE
                } else {
                    if (todaysAppointList?.get(pos)?.appointmentStatus != null && !TextUtils.isEmpty(todaysAppointList?.get(pos)?.appointmentStatus?.trim()) &&
                        todaysAppointList?.get(pos)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("completed")!!
                    ) {
                        itemVie.llMainlayout.background = ContextCompat.getDrawable(context, R.drawable.background_green_stroke_box)
                        btnCompleted.visibility = View.GONE
                        btnReject.visibility = View.GONE
                    } else {
                        itemVie.llMainlayout.background = null
                        btnCompleted.visibility = View.VISIBLE
                        btnCompleted.setText("Complete")
                        btnCompleted.setOnClickListener {
                            recyclerViewItemClickWithView2.onAcceptBtnClick(local_position.toString(), "")
                        }
                        if (todaysAppointList?.get(pos)?.acceptanceStatus != null && !TextUtils.isEmpty(todaysAppointList?.get(pos)?.acceptanceStatus?.trim()) &&
                            todaysAppointList?.get(pos)?.acceptanceStatus?.toLowerCase(Locale.ROOT)?.contains("reject")!!
                        ) {
                            btnReject.visibility = View.GONE
                        } else {
//                            btnReject.visibility = View.VISIBLE
                        }
                    }
                }
            }


            if (todaysAppointList?.get(local_position)?.appointmentStatus != null) {
                if (todaysAppointList?.get(local_position)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("complete")!!) {
                    itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).setText("Completed")
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                        .setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
                }else if (todaysAppointList?.get(local_position)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("reject")!!) {
                    itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).setText("Rejected")
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                        .setTextColor(ContextCompat.getColor(context, R.color.red))
                }else if (todaysAppointList?.get(local_position)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("cancel")!!) {
                    itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).setText("Cancelled")
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                        .setTextColor(ContextCompat.getColor(context, R.color.red))
                }else if (todaysAppointList?.get(local_position)?.appointmentStatus?.toLowerCase(Locale.ROOT)?.contains("book")!!) {
                    itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.VISIBLE
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus).setText("Booked")
                    itemView.rootView.findViewById<TextView>(R.id.txtAppointmnetStatus)
                        .setTextColor(ContextCompat.getColor(context, R.color.orange))
                }else{
                    itemView.rootView.findViewById<LinearLayout>(R.id.llAppointmentStatus).visibility = View.GONE
                }
            }else{
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
