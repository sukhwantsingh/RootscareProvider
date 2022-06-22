package com.rootscare.serviceprovider.ui.pricelistss.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.LayoutNewItemPriceBinding
import com.rootscare.serviceprovider.ui.pricelistss.ModelPriceListing
import com.rootscare.serviceprovider.utilitycommon.changeColorWithCheck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList


class AdapterPriceListCommon(internal var context: Context) :
    ListAdapter<ModelPriceListing.Result, AdapterPriceListCommon.ViewHolder>(PriceListDiffUtil()) {

    companion object {
        val TAG: String = AdapterPriceListCommon::class.java.simpleName
    }
    val updatedArrayList = ArrayList<ModelPriceListing.Result?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<LayoutNewItemPriceBinding>(
            LayoutInflater.from(context),
            R.layout.layout_new_item_price_, parent, false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView()
    }

    override fun submitList(list: MutableList<ModelPriceListing.Result?>?) {
        super.submitList(if(list.isNullOrEmpty()) null else ArrayList(list))
    }
    fun updateStatus(pos: Int, checkStatus: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            updatedArrayList[pos]?.isChecked = checkStatus
        }

        //    notifyItemChanged(pos)
      //  submitList(ArrayList(updatedArrayList))
    }

    fun updatePrice(pos: Int, price: String) {
        CoroutineScope(Dispatchers.IO).launch {
            updatedArrayList[pos]?.price = price
        }
    //    notifyItemChanged(pos)
    }
    fun getUpdatedList() = updatedArrayList

    inner class ViewHolder(val binding: LayoutNewItemPriceBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.swTask.setOnClickListener {
                updateStatus(absoluteAdapterPosition, binding.swTask.isChecked)
                setDataOnSwitch(binding, binding.swTask.isChecked)
            }
            binding.edtPrice.addTextChangedListener {
                updatePrice(absoluteAdapterPosition, it.toString())
            }
        }

        fun onBindView() {
            setData(binding, absoluteAdapterPosition)
        }

    }

 fun setData(binding: LayoutNewItemPriceBinding, pos:Int) {
     binding.run {
         swTask.isChecked = updatedArrayList[pos]?.isChecked ?: false
         tvTask.text = updatedArrayList[pos]?.name

         edtPrice.isEnabled = updatedArrayList[pos]?.isChecked ?: false
         edtPrice.setText(updatedArrayList[pos]?.price ?: "")

         edtPrice.changeColorWithCheck(updatedArrayList[pos]?.isChecked ?: false)
         tvTask.changeColorWithCheck(updatedArrayList[pos]?.isChecked ?: false)

     }
 }

 fun setDataOnSwitch(binding: LayoutNewItemPriceBinding, ckStatus: Boolean?){
     binding.run {
         edtPrice.isEnabled = ckStatus?: false
         edtPrice.changeColorWithCheck(ckStatus?: false)
         tvTask.changeColorWithCheck(ckStatus ?: false)

     }
 }

}

class PriceListDiffUtil : DiffUtil.ItemCallback<ModelPriceListing.Result>() {
    override fun areItemsTheSame(oldItem: ModelPriceListing.Result, newItem: ModelPriceListing.Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ModelPriceListing.Result, newItem: ModelPriceListing.Result): Boolean {
        return oldItem.id == newItem.id && oldItem.isChecked == newItem.isChecked &&
                oldItem.name == newItem.name && oldItem.price == newItem.price
    }

}