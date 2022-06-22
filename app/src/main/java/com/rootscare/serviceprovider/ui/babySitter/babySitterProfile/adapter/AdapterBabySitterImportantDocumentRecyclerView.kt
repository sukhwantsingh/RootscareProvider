package com.rootscare.serviceprovider.ui.babySitter.babySitterProfile.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemDoctorImportantDocumentRecyclerviewBinding
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls
import kotlinx.android.synthetic.main.item_doctor_important_document_recyclerview.view.*

class AdapterBabySitterImportantDocumentRecyclerView(
    val qualificationDataList: ArrayList<com.rootscare.data.model.response.caregiver.profileresponse.QualificationDataItem>?,
    internal var context: Context
) : RecyclerView.Adapter<AdapterBabySitterImportantDocumentRecyclerView.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterBabySitterImportantDocumentRecyclerView::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
    internal lateinit var recyclerViewItemClickWithView: OnItemClickWithReportIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemDoctorImportantDocumentRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_doctor_important_document_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return qualificationDataList?.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(internal var itemVie: ItemDoctorImportantDocumentRecyclerviewBinding) : RecyclerView.ViewHolder(itemVie.root) {

        private var localPosition: Int = 0

        init {
            itemVie.parentLayout.setOnClickListener {
                recyclerViewItemClickWithView.onItemClick(localPosition)
            }

        }

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            localPosition = pos

            itemView.txt_description?.text = qualificationDataList?.get(pos)?.qualification

            if (qualificationDataList?.get(pos)?.qualificationCertificate != null && !TextUtils.isEmpty(qualificationDataList[pos].qualificationCertificate?.trim()) &&
                !qualificationDataList[pos].qualificationCertificate?.contains(".pdf")!!
            ) {
                val circularProgressDrawable = CircularProgressDrawable(context)
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.setColorSchemeColors(*intArrayOf(R.color.green_light_1, R.color.green))
                circularProgressDrawable.start()
                val requestOptions: RequestOptions =
                    RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.prescription)
                        .priority(Priority.HIGH)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                val imageUrl = BaseMediaUrls.USERIMAGE.url + qualificationDataList[pos].qualificationCertificate
                Glide.with(context).load(imageUrl).apply(RequestOptions().placeholder(circularProgressDrawable)).timeout(1000 * 60)
                    .apply(requestOptions).into(itemVie.imgContentimage)
            } else {
                Glide.with(context).load(R.drawable.pdf_file_logo).into(itemVie.imgContentimage)
            }

        }
    }

}