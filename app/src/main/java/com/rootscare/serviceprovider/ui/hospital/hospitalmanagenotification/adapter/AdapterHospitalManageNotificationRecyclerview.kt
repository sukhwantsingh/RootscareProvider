package com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.hospital.NotificationItemResult
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutItemNewNotificationsBinding
import java.util.ArrayList

interface OnNotificationCallback{
    fun onNotificationClick(node: NotificationItemResult?, pos:Int)
}

class AdapterHospitalManageNotificationRecyclerview(
    val notificationItemResult: ArrayList<NotificationItemResult?>?,
    internal var context: Context
) :
    RecyclerView.Adapter<AdapterHospitalManageNotificationRecyclerview.ViewHolder>() {
    companion object {
        val TAG: String = AdapterHospitalManageNotificationRecyclerview::class.java.simpleName
    }

    internal lateinit var mCalllback: OnNotificationCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = DataBindingUtil.inflate<LayoutItemNewNotificationsBinding>(
            LayoutInflater.from(context),
             R.layout.layout_item_new_notifications, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notificationItemResult?.size?: 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindViews(notificationItemResult?.get(position))
    }

    fun updatedToRead(pos:Int) {
        notificationItemResult?.get(pos)?.read = "1"
        notifyItemChanged(pos)
    }

  //  inner class ViewHolder(itemView: ItemHospitalManageNotificationRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {
    inner class ViewHolder(val binding: LayoutItemNewNotificationsBinding) : RecyclerView.ViewHolder(binding.root) {

      init {
          binding.cnsNotiRoot.setOnClickListener {
           mCalllback.onNotificationClick(notificationItemResult?.get(absoluteAdapterPosition), absoluteAdapterPosition)
         //     updatedToRead(absoluteAdapterPosition)
          }

      }

        fun onBindViews(mData: NotificationItemResult?) {
       //   itemView.rootView.txt_view_schedule?.text = notificationItemResult?.get(absoluteAdapterPosition)?.body
              binding.run {
                tvNotficationText.text = mData?.body ?: "Not Defined"
                tvDate.text = (mData?.datetime ?: "") +" \u2022 "+ (mData?.date?:"")

                if(mData?.read?.equals("1",ignoreCase = true)==true){
                      ivRocket.setImageResource(R.drawable.ic_rocket_read_)
                      vl1.setBackgroundColor(Color.parseColor("#515c6f"))
                  }else {
                      ivRocket.setImageResource(R.drawable.ic_rocket_unread)
                      vl1.setBackgroundColor(Color.parseColor("#0888D1"))
                  }

              }

        }
    }

}