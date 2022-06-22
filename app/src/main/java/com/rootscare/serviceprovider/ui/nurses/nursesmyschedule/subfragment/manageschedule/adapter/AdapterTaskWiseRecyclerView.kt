package com.rootscare.serviceprovider.ui.nurses.nursesmyschedule.subfragment.manageschedule.adapter

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.data.model.response.loginresponse.ResultItem
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ItemNurseTaskRateListRecyclerviewBinding
import kotlinx.android.synthetic.main.item_nurse_task_rate_list_recyclerview.view.*
import java.util.*


class AdapterTaskWiseRecyclerView(val resultItemArrayList: ArrayList<ResultItem?>?, internal var context: Context) :
    RecyclerView.Adapter<AdapterTaskWiseRecyclerView.ViewHolder>() {

    internal lateinit var onItemClickListener: OnItemClickListener

    companion object {
        val TAG: String = AdapterTaskWiseRecyclerView::class.java.simpleName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val singleItemDashboardListingBinding = DataBindingUtil.inflate<ItemNurseTaskRateListRecyclerviewBinding>(
            LayoutInflater.from(context),
            R.layout.item_nurse_task_rate_list_recyclerview, parent, false
        )
        return ViewHolder(singleItemDashboardListingBinding)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return resultItemArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(position)
    }


    inner class ViewHolder(itemView: ItemNurseTaskRateListRecyclerviewBinding) : RecyclerView.ViewHolder(itemView.root) {

        private var localPosition: Int = 0

        fun onBind(pos: Int) {
            Log.d(TAG, " true")
            localPosition = pos

            if (localPosition % 2 == 1) {
                itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            } else {
                itemView.setBackgroundColor(Color.parseColor("#EBF8F5"));
            }

            itemView.rootView?.tv_slot?.text = resultItemArrayList?.get(localPosition)?.name
            itemView.rootView?.et_price?.tag = resultItemArrayList?.get(localPosition)
            itemView.rootView?.et_price?.setText(resultItemArrayList?.get(localPosition)?.price)

            itemView.rootView?.et_price?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    resultItemArrayList?.get(localPosition)?.price = s.toString()
                }

                override fun afterTextChanged(s: Editable) {}
            })

            itemView.rootView?.et_price?.isEnabled = resultItemArrayList?.get(localPosition)?.isChecked == true

            //without this addTextChangedListener my code works fine obviously
            // not saving the content of the edit Text when scrolled
            // If i add this code then when i scroll all textView that go of screen
            // and than come back in have messed up content
            itemView.rootView?.et_price?.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    //setting data to array, when changed
                    // this is a simplified example in the actual app i save the text
                    // in  a.txt in the external storage
                    resultItemArrayList?.get(pos)?.price = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {}
            })

            //in some cases, it will prevent unwanted situations
            itemView.rootView?.cb_task?.setOnCheckedChangeListener(null)

            //if true, your checkbox will be selected, else unselected
            itemView.rootView?.cb_task?.isChecked = resultItemArrayList?.get(localPosition)?.isChecked == true

//            itemView.rootView?.cb_task?.setOnClickListener {
//                onItemClickListener.onCheck(
//                    localPosition, itemView.rootView?.cb_task?.isChecked == true
//                )
//            }

            itemView.rootView?.cb_task?.setOnCheckedChangeListener { _, isChecked ->
                onItemClickListener.onCheck(localPosition, isChecked)
            }
        }

    }


    interface OnItemClickListener {
        fun onCheck(position: Int, checked: Boolean)

    }
}