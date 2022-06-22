package com.rootscare.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.rootscare.interfaces.OnItemClickListener
import com.rootscare.model.DrawerDatatype
import com.rootscare.serviceprovider.R

import java.util.LinkedList

class DrawerAdapter(private val context: Context, drawerDatatypes: LinkedList<DrawerDatatype>) :
    RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {

    internal lateinit var recyclerView: RecyclerView
    var expandedPosition = -1
    private var onItemClickListener: OnItemClickListener? = null
    private var drawerDataTypes = LinkedList<DrawerDatatype>()

    init {
        this.drawerDataTypes = drawerDatatypes
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.navigation_drawer_single_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val layoutManager = recyclerView.layoutManager
        holder.item_tv.width = layoutManager!!.width

        holder.item_parent_ll.setOnClickListener {
            if (expandedPosition >= 0) {
                val prev = expandedPosition
                notifyItemChanged(prev)
            }

            if (position == expandedPosition) {
                /*expandedPosition = -1;
                    notifyDataSetChanged();*/

                // For Multiple click on last item
                if (position == drawerDataTypes.size - 1) {
                    if (onItemClickListener != null) {
                        onItemClickListener!!.onItemClick(position, holder.item_tv)
                    }
                }
            } else {
                expandedPosition = position
                notifyDataSetChanged()
                if (onItemClickListener != null)
                    onItemClickListener!!.onItemClick(position, holder.item_tv)
            }
        }

        holder.item_tv.text = drawerDataTypes[position].string_item
        if (drawerDataTypes[position].notification_badge_count != 0) {
            Glide.with(context)
                .load(drawerDataTypes[position].notification_badge_count)
                .into(holder.imgcheck!!)
        }

//        holder.imgcheck=drawerDatatypes[position].notification_badge_count

        /*SpannableString spannableString = new SpannableString(strings.get(position).getString_item());
        spannableString.setSpan(new UnderlineSpan(), 0, strings.get(position).getString_item().length(), 0);
        holder.item_tv.setText(spannableString);*/
        if (position == expandedPosition) {
            holder.item_tv.setTypeface(null, Typeface.BOLD)
        } else {
            holder.item_tv.setTypeface(null, Typeface.NORMAL)
        }

    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun getItemCount(): Int {
        return drawerDataTypes.size
    }

    inner class ViewHolder
    //        ImageView notification_count_img;

        (itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var item_parent_ll: RelativeLayout = itemView.findViewById(R.id.rl_sidemenue_dashboard)
        internal var rl_noti_count: RelativeLayout? = null
        internal var item_tv: TextView = itemView.findViewById(R.id.item_tv)
        internal var tv_noti_count: TextView? = null
        internal var view1: View? = null
        internal var imgcheck: ImageView? = null

        init {

            imgcheck = itemView.findViewById(R.id.check)
            //            view1 = itemView.findViewById(R.id.view1);
            //            rl_noti_count = itemView.findViewById(R.id.rl_noti_count);
            //            tv_noti_count = itemView.findViewById(R.id.tv_noti_count);
            //            notification_count_img = itemView.findViewById(R.id.notification_count_img);
        }


    }

    fun setonClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }


//    fun selectItem(position: Int) {
//        expandedPosition = position
//        notifyDataSetChanged()
//    }


    fun changeNotificationBadge(badge_count: String) {
        drawerDataTypes[4].notification_badge_count = Integer.parseInt(badge_count)
        notifyItemChanged(4)
    }


    fun setSelection(position: Int) {
        this.expandedPosition = position
        notifyDataSetChanged()
    }
}