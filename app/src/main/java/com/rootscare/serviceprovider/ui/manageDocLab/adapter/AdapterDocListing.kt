package com.rootscare.serviceprovider.ui.manageDocLab.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewItemDocListingBinding
import com.rootscare.serviceprovider.ui.manageDocLab.model.ModelHospitalDocs
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.ModelAppointmentsListing
import com.rootscare.serviceprovider.ui.pricelistss.ModelPriceListing
import com.rootscare.serviceprovider.utilitycommon.setCircularRemoteImage
import java.util.ArrayList

interface OnHospitalDocsCallback{
    fun onDocsLabEdit(docId:String?)
}
class AdapterDocListing(internal var context: Context) :
    ListAdapter<ModelHospitalDocs.Result, AdapterDocListing.ViewHolder>(DocListingDiffUtil()) {

    val updatedArrayList = ArrayList<ModelHospitalDocs.Result?>()
    var mCallback: OnHospitalDocsCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutNewItemDocListingBinding>(
            LayoutInflater.from(context),
            R.layout.layout_new_item_doc_listing, parent, false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView(getItem(position))
    }

    override fun submitList(list: MutableList<ModelHospitalDocs.Result?>?) {
        super.submitList(if(list.isNullOrEmpty()) null else ArrayList(list))
    }

    fun loadDataIntoList(list: ArrayList<ModelHospitalDocs.Result?>?) {
        updatedArrayList.clear()
        list?.let { updatedArrayList.addAll(it.toMutableList()) }
        submitList(ArrayList(updatedArrayList))
    }

    fun getUpdatedList() = updatedArrayList

    inner class ViewHolder(val binding: LayoutNewItemDocListingBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
             binding.tvEditprofile.setOnClickListener {
                 mCallback?.onDocsLabEdit(getItem(absoluteAdapterPosition).user_id)
            }
        }

        fun onBindView(item: ModelHospitalDocs.Result?) {
            binding.run {
                item?.let {
                    imgProfile.setCircularRemoteImage(it.image)
                    tvUsername.text = it.name
                    tvhSpecialityHeader.text = it.speciality
                    tvemail.text = it.email
                    tvphn.text = it.phone_number
                    tvBankName.text = if (it.experience.isNullOrBlank().not()) "${it.experience} YR" else "-"  // exp
                    tvAcName.text = it.booking_count?.toString() ?: "0"   // bookings count
                    tvAcNum.text = it.avg_rating?.toFloat().toString()  //avg  rating
                    binding.executePendingBindings()
                }
            }
        }

    }
}

class DocListingDiffUtil : DiffUtil.ItemCallback<ModelHospitalDocs.Result>() {
    override fun areItemsTheSame(oldItem: ModelHospitalDocs.Result, newItem: ModelHospitalDocs.Result): Boolean {
        return oldItem.user_id == newItem.user_id
    }

    override fun areContentsTheSame(oldItem: ModelHospitalDocs.Result, newItem: ModelHospitalDocs.Result): Boolean {
        return oldItem.user_id == newItem.user_id && oldItem.image == newItem.image
    }

}