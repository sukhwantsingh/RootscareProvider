package com.rootscare.serviceprovider.ui.labtechnician.labtechnicianmyspeciality.adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.interfaces.OnItemClikWithIdListener
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemLabtechnicianSpecialitiesRecyclerviewBinding
import kotlinx.android.synthetic.main.item_labtechnician_specialities_recyclerview.view.*

class AdapterLabTechnicianMySpecilitiesList(internal var context: Context) :
    RecyclerView.Adapter<AdapterLabTechnicianMySpecilitiesList.ViewHolder>() {
    //    val trainerList: ArrayList<TrainerListItem?>?,
    companion object {
        val TAG: String = AdapterLabTechnicianMySpecilitiesList::class.java.simpleName
    }

    //    internal lateinit var recyclerViewItemClick: ItemStudyMaterialRecyclerviewOnItemClick
//
    internal lateinit var recyclerViewItemClickWithView: OnItemClikWithIdListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemLabtechnicianSpecialitiesRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_labtechnician_specialities_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemCount(): Int {
//        return trainerList!!.size
        return 8
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemLabtechnicianSpecialitiesRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            localPosition = pos
            if ((localPosition % 2) == 0) {
                itemView.rootView?.ll_main_content?.setBackgroundColor(Color.parseColor("#EBF8F5"))

            } else {
                itemView.rootView?.ll_main_content?.setBackgroundColor(Color.parseColor("#FFFFFF"))
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
    }

}