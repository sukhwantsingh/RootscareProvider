package com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.editBabySitterProfile.adapter

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
import kotlin.collections.ArrayList

class BabySitterCertificateListAdapter(internal var context: Context) : RecyclerView.Adapter<BabySitterCertificateListAdapter.ViewHolder>() {
    companion object {
        val TAG: String = BabySitterCertificateListAdapter::class.java.simpleName
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

        private var localPosition: Int = 0

        init {
//            itemView?.root?.crdview_appoitment_list?.setOnClickListener(View.OnClickListener {
//                recyclerViewItemClickWithView?.onItemClick(1)
//            })
//            itemView?.root?.btn_view_trainner_profile?.setOnClickListener(View.OnClickListener {
            with(itemVie) {
                imageViewClose.setOnClickListener {
                    qualificationDataList.removeAt(localPosition)
                    notifyItemRemoved(localPosition)
                    notifyItemRangeChanged(localPosition, qualificationDataList.size)
                }
            }
//
        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            localPosition = pos
            with(itemVie) {
                tvSerialNumber.text = "${localPosition + 1})"

                if (qualificationDataList[localPosition].qualification != null && !TextUtils.isEmpty(qualificationDataList[localPosition].qualification?.trim())) {
                    textViewQualification.text = qualificationDataList[localPosition].qualification
                }
                if (qualificationDataList[localPosition].institute != null && !TextUtils.isEmpty(qualificationDataList[localPosition].institute?.trim())) {
                    textViewInstitute.text = qualificationDataList[localPosition].institute
                }
                if (qualificationDataList[localPosition].passingYear != null && !TextUtils.isEmpty(qualificationDataList[localPosition].passingYear?.trim())) {
                    textViewPassingYear.text = "${qualificationDataList[localPosition].passingYear}"
                }
                if (qualificationDataList[localPosition].qualificationCertificate != null &&
                    !TextUtils.isEmpty(qualificationDataList[localPosition].qualificationCertificate?.trim())
                ) {
                    textViewCertificate.text = qualificationDataList[localPosition].qualificationCertificate
                }
                if (qualificationDataList[localPosition].isOldData) {
                    imageViewClose.visibility = View.INVISIBLE
                } else {
                    imageViewClose.visibility = View.VISIBLE
                }
            }

        }
    }

}