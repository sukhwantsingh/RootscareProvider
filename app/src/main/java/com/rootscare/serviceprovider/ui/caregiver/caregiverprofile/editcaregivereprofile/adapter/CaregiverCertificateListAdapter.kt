package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.editcaregivereprofile.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemCertificateListLayoutBinding

class CaregiverCertificateListAdapter(internal var context: Context) : RecyclerView.Adapter<CaregiverCertificateListAdapter.ViewHolder>() {
    companion object {
        val TAG: String = CaregiverCertificateListAdapter::class.java.simpleName
    }

    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener
    val qualificationDataList: ArrayList<com.rootscare.data.model.response.caregiver.profileresponse.QualificationDataItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemCertificateListLayoutBinding>(
            LayoutInflater.from(context),
            R.layout.item_certificate_list_layout, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
        return qualificationDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(internal var itemVie: ItemCertificateListLayoutBinding) : RecyclerView.ViewHolder(itemVie.root) {

        private var local_position: Int = 0

        init {
//            itemView?.root?.crdview_appoitment_list?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(1)
//            })
//            itemView?.root?.btn_view_trainner_profile?.setOnClickListener(View.OnClickListener {
            with(itemVie) {
                imageViewClose.setOnClickListener {
                    qualificationDataList.removeAt(local_position)
                    notifyItemRemoved(local_position)
                    notifyItemRangeChanged(local_position, qualificationDataList.size)
                }
            }
//
        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            local_position = pos
            with(itemVie) {
                tvSerialNumber.setText("${local_position + 1})")

                if (qualificationDataList[local_position].qualification != null && !TextUtils.isEmpty(qualificationDataList[local_position].qualification?.trim())) {
                    textViewQualification.text = qualificationDataList[local_position].qualification
                }
                if (qualificationDataList[local_position].institute != null && !TextUtils.isEmpty(qualificationDataList[local_position].institute?.trim())) {
                    textViewInstitute.text = qualificationDataList[local_position].institute
                }
                if (qualificationDataList[local_position].passingYear != null && !TextUtils.isEmpty(qualificationDataList[local_position].passingYear?.trim())) {
                    textViewPassingYear.text = "${qualificationDataList[local_position].passingYear}"
                }
                if (qualificationDataList[local_position].qualificationCertificate != null &&
                    !TextUtils.isEmpty(qualificationDataList[local_position].qualificationCertificate?.trim())
                ) {
                    textViewCertificate.text = qualificationDataList[local_position].qualificationCertificate
                }
                if (qualificationDataList[local_position].isOldData) {
                    imageViewClose.visibility = View.INVISIBLE
                } else {
                    imageViewClose.visibility = View.VISIBLE
                }
            }

        }
    }

}