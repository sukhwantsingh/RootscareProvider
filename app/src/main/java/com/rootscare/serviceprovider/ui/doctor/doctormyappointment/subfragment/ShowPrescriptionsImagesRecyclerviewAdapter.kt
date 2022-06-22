package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rootscare.data.model.response.doctor.appointment.appointmentdetails.Prescription
import com.rootscare.interfaces.OnClickOfDoctorAppointment
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemShowPrescriptionSingleItemBinding
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls
import java.util.*

class ShowPrescriptionsImagesRecyclerviewAdapter (internal var todaysAppointList: LinkedList<Prescription>?, internal var context: Context) : RecyclerView.Adapter<ShowPrescriptionsImagesRecyclerviewAdapter.ViewHolder>() {
    companion object {
        val TAG: String = ShowPrescriptionsImagesRecyclerviewAdapter::class.java.simpleName
    }

    internal lateinit var recyclerViewItemClickWithView2: OnClickOfDoctorAppointment
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding =
            DataBindingUtil.inflate<ItemShowPrescriptionSingleItemBinding>(
                LayoutInflater.from(context),
                R.layout.item_show_prescription_single_item, parent, false
            )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return todaysAppointList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(internal var itemVie: ItemShowPrescriptionSingleItemBinding) :
        RecyclerView.ViewHolder(itemVie.root) {

        private var local_position: Int = 0

        init {
            itemVie.llPrescription.setOnClickListener(View.OnClickListener {
                recyclerViewItemClickWithView2.onItemClick(local_position)
            })

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos

            with(itemVie) {
                if (todaysAppointList!![local_position].prescription_number != null && !TextUtils.isEmpty(todaysAppointList!![local_position].prescription_number?.trim())) {
                    tvPrescriptionValue.text = "Prescription Number:  ${todaysAppointList!![local_position].prescription_number}"
                }

                if (todaysAppointList!![local_position].e_prescription != null && !TextUtils.isEmpty(todaysAppointList!![local_position].e_prescription?.trim())) {
                    val options: RequestOptions =
                        RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.profile_no_image)
                            .priority(Priority.HIGH)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)

                    Glide
                        .with(context)
                        .load(BaseMediaUrls.USERIMAGE.url + todaysAppointList!![local_position].e_prescription)
                        .apply(options)
                        .into(imageViewPrescription)
                }
            }

        }
    }
}
