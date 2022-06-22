package com.rootscare.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.rootscare.model.MenuModel
import com.rootscare.serviceprovider.R
import java.util.*


class ExpandableListAdapter(
    private val context: Context, private val listDataHeader: ArrayList<MenuModel>,
    private val listDataChild: HashMap<MenuModel, ArrayList<MenuModel>?>
) : BaseExpandableListAdapter() {
    override fun getChild(groupPosition: Int, childPosititon: Int): MenuModel {
        return listDataChild[listDataHeader[groupPosition]]
            ?.get(childPosititon)!!
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View?, parent: ViewGroup
    ): View {
        var convertView = convertView
        val childText = getChild(groupPosition, childPosition).menuName
        if (convertView == null) {
            val infalInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_group_child, null)
        }
        val txtListChild = convertView
            ?.findViewById<TextView>(R.id.lblListItem)
        txtListChild?.text = childText
        return convertView!!
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return if (listDataChild[listDataHeader[groupPosition]] == null) 0 else listDataChild[listDataHeader[groupPosition]]!!
            .size
    }

    override fun getGroup(groupPosition: Int): MenuModel {
        return listDataHeader[groupPosition]
    }

    override fun getGroupCount(): Int {
        return listDataHeader.size
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View?, parent: ViewGroup
    ): View {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition).menuName
        if (convertView == null) {
            val infalInflater = context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_group_header, null)
        }
        val lblListHeader = convertView?.findViewById<TextView>(R.id.lblListHeader)
        lblListHeader?.setTypeface(null, Typeface.BOLD)
        lblListHeader?.text = headerTitle
        if(getGroup(groupPosition).hasChildren){

         //   lblListHeader?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.side_drop_down1, 0, R.drawable.side_drop_down2, 0)
        }else{
        //    lblListHeader?.setCompoundDrawablesWithIntrinsicBounds( R.drawable.side_drop_down1, 0, 0, 0);

        }

        return convertView!!
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

}