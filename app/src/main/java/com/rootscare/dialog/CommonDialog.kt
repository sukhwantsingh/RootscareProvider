package com.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rootscare.adapter.AdapterCommonDropdown
import com.rootscare.adapter.AdapterDropdownDepartment
import com.rootscare.customviews.MyCustomButton
import com.rootscare.data.model.response.hospital.HospitalResultItem
import com.rootscare.interfaces.*
import com.rootscare.serviceprovider.R

@SuppressLint("StaticFieldLeak")
object CommonDialog {

    private val TAG = "CommonDialog"

    fun showDialog(
        activity: Context, dialogClickCallback: DialogClickCallback, dialog_title: String,
        dialog_message: String, pBtnText:String? = "Yes"
    ) {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder.setTitle(dialog_title)
        alertDialogBuilder.setMessage(dialog_message)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton(pBtnText) { dialogInterface, _ ->
            dialogClickCallback.onConfirm()
            dialogInterface.dismiss()
        }
        alertDialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialogClickCallback.onDismiss()
            dialog.dismiss()
        }
        alertDialogBuilder.show()
    }

    fun showDialogForSingleButton(
        activity: Context, dialogClickCallback: DialogClickCallback, dialog_title: String?,
        dialog_message: String
    ) {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        if(dialog_title.isNullOrBlank().not()){
            alertDialogBuilder.setTitle(dialog_title)
        }
        alertDialogBuilder.setMessage(dialog_message)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setPositiveButton(R.string.ok) { dialogInterface, i ->
            dialogClickCallback.onConfirm()
            dialogInterface.dismiss()
        }
        alertDialogBuilder.show()
    }


    // Add appointment to calendar after successful appointment creation
//    fun showDialogForAskingAddress(context: Context, title: String,dialogClickCallback: DialogClickCallback) {
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.ask_for_address_dialog_layout)
//        dialog.setCancelable(false)
//        dialog.setCanceledOnTouchOutside(false)
//
//        val cancel = dialog.findViewById<TextView>(R.id.btn_negative)
//        val confirm = dialog.findViewById<TextView>(R.id.btn_positive)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        if (title.equals("")){
//            tv_title?.setText("Thank You for filling up admission form. You will shortly be informed about the further process. You will only be able to login to app once your admission is confirmed.")
//        }else{
//            tv_title?.setText(title)
//        }
//
//        cancel.setOnClickListener {
//            dialogClickCallback.onDismiss()
//            dialog.dismiss()
//        }
//        confirm.setOnClickListener {
//            dialogClickCallback.onConfirm()
//            dialog.dismiss()
//        }
//        dialog.show()
//    }


    fun showDialogForAddPatient(context: Context, title: String, dialogClickCallback: DialogClickCallback) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_add_patient)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val cancel = dialog.findViewById<TextView>(R.id.btn_negative)
        val confirm = dialog.findViewById<TextView>(R.id.btn_positive)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        if (title.equals("")){
//            tv_title?.setText("Thank You for filling up admission form. You will shortly be informed about the further process. You will only be able to login to app once your admission is confirmed.")
//        }else{
//            tv_title?.setText(title)
//        }

        cancel.setOnClickListener {
            dialogClickCallback.onDismiss()
            dialog.dismiss()
        }
        confirm.setOnClickListener {
            dialogClickCallback.onConfirm()
            dialog.dismiss()
        }
        dialog.show()
    }


    fun showDialogForDropDownList(
        context: Context,
        title: String,
        dropdownList: ArrayList<String?>?,
        dialogClickCallback: DropDownDialogCallBack
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_dropdown)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        val recyclerView_dropdown_list = dialog.findViewById<RecyclerView>(R.id.recyclerView_dropdown_list)
        val tv_title = dialog.findViewById<TextView>(R.id.txt_header_title)
        tv_title?.text = title
        val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        recyclerView_dropdown_list.layoutManager = gridLayoutManager
        val dropdownListAdapter = AdapterCommonDropdown(dropdownList, context)
        recyclerView_dropdown_list.adapter = dropdownListAdapter
        dropdownListAdapter.recyclerViewItemClick = object : OnDropDownListItemClickListener {
            override fun onConfirm(text: String) {
                dialogClickCallback.onConfirm(text)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    fun showDialogForAddHospitalOrClinic(context: Context, dialogClickCallback: DialogClickCallbackWithFields) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_add_hospita_or_clinic)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)

        val cancel = dialog.findViewById<TextView>(R.id.btn_negative)
        val confirm = dialog.findViewById<TextView>(R.id.btn_positive)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        if (title.equals("")){
//            tv_title?.setText("Thank You for filling up admission form. You will shortly be informed about the further process. You will only be able to login to app once your admission is confirmed.")
//        }else{
//            tv_title?.setText(title)
//        }

        cancel.setOnClickListener {
            dialogClickCallback.onDismiss()
            dialog.dismiss()
        }

        val hospitaName = dialog.findViewById<TextView>(R.id.etHospitalName)
        val address = dialog.findViewById<TextView>(R.id.etAddress)

        confirm.setOnClickListener {
            if (!TextUtils.isEmpty(hospitaName.text.trim()) && !TextUtils.isEmpty(address.text.trim())) {
                dialogClickCallback.onConfirm(hospitalName = hospitaName.text.toString().trim(), address = address.text.toString().trim())
                dialog.dismiss()
            } else {
                if (TextUtils.isEmpty(hospitaName.text.trim())) {
                    hospitaName.error = context.getString(R.string.can_not_be_blank)
                    hospitaName.requestFocus()
                }
                if (TextUtils.isEmpty(address.text.trim())) {
                    address.error = context.getString(R.string.can_not_be_blank)
                    address.requestFocus()
                }
            }
        }
        dialog.show()
    }


    fun showDialogForAddReason(context: Context, dialogClickCallback: DialogClickCallback) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_add_reason)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val cancel = dialog.findViewById<TextView>(R.id.btn_negative)
        val confirm = dialog.findViewById<TextView>(R.id.btn_positive)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        if (title.equals("")){
//            tv_title?.setText("Thank You for filling up admission form. You will shortly be informed about the further process. You will only be able to login to app once your admission is confirmed.")
//        }else{
//            tv_title?.setText(title)
//        }

        cancel.setOnClickListener {
            dialogClickCallback.onDismiss()
            dialog.dismiss()
        }
        confirm.setOnClickListener {
            dialogClickCallback.onConfirm()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDialogForAddSpeciality(context: Context, dialogClickCallback: DialogClickCallback) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_hospital_add_sepciality)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val cancel = dialog.findViewById<TextView>(R.id.btn_negative)
        val confirm = dialog.findViewById<TextView>(R.id.btn_positive)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        if (title.equals("")){
//            tv_title?.setText("Thank You for filling up admission form. You will shortly be informed about the further process. You will only be able to login to app once your admission is confirmed.")
//        }else{
//            tv_title?.setText(title)
//        }

        cancel.setOnClickListener {
            dialogClickCallback.onDismiss()
            dialog.dismiss()
        }
        confirm.setOnClickListener {
            dialogClickCallback.onConfirm()
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showDialogForDelete(context: Context, dialogClickCallback: DialogClickCallback) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.want_to_delete)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(true)

        val cancel = dialog.findViewById<TextView>(R.id.btn_negative)
        val confirm = dialog.findViewById<TextView>(R.id.btn_positive)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        if (title.equals("")){
//            tv_title?.setText("Thank You for filling up admission form. You will shortly be informed about the further process. You will only be able to login to app once your admission is confirmed.")
//        }else{
//            tv_title?.setText(title)
//        }

        cancel.setOnClickListener {
            dialogClickCallback.onDismiss()
            dialog.dismiss()
        }
        confirm.setOnClickListener {
            dialogClickCallback.onConfirm()
            dialog.dismiss()
        }
        dialog.show()
    }


    fun showDialogForAddLabtechnicianSpeciality(context: Context, dialogClickCallback: DialogClickCallback) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_add_specialities)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val cancel = dialog.findViewById<TextView>(R.id.btn_negative)
        val confirm = dialog.findViewById<TextView>(R.id.btn_positive)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        if (title.equals("")){
//            tv_title?.setText("Thank You for filling up admission form. You will shortly be informed about the further process. You will only be able to login to app once your admission is confirmed.")
//        }else{
//            tv_title?.setText(title)
//        }

        cancel.setOnClickListener {
            dialogClickCallback.onDismiss()
            dialog.dismiss()
        }
        confirm.setOnClickListener {
            dialogClickCallback.onConfirm()
            dialog.dismiss()
        }
        dialog.show()
    }

    //Show Dialog For DepartmentDropdown

//    fun showDialogForDepartmentDropDownList(
//        context: Context,
//        departmentList: ArrayList<ResultItem?>?,
//        title: String,
//        dialogClickCallback: OnDepartmentDropDownListItemClickListener
//    ) {
//        val dialog = Dialog(context)
//        dialog.setContentView(R.layout.dialog_department_dropdown)
//        dialog.setCancelable(true)
//        dialog.setCanceledOnTouchOutside(false)
//        val btn_dropdown_ok = dialog.findViewById<MyCustomButton>(R.id.btn_dropdown_ok)
//        val btn_dropdown_cancel = dialog.findViewById<MyCustomButton>(R.id.btn_dropdown_cancel)
//        val recyclerView_department_dropdown_list = dialog.findViewById<RecyclerView>(R.id.recyclerView_department_dropdown_list)
//        val tv_title = dialog.findViewById<TextView>(R.id.txt_departmentheader_title)
//        tv_title?.setText(title)
//        var getDepartmentString = ""
//        var getDepartmentIdString = ""
//        var selectDepartmentList: ArrayList<ResultItem?>? = null
//        val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
//        recyclerView_department_dropdown_list.layoutManager = gridLayoutManager
//        recyclerView_department_dropdown_list.setHasFixedSize(true)
//        val dropdownListAdapter = AdapterDropdownDepartment(departmentList, context)
//        recyclerView_department_dropdown_list.adapter = dropdownListAdapter
//        dropdownListAdapter.recyclerViewItemClick = object : OnDepartmentDropDownListItemClickListener {
//            override fun onConfirm(departmentList: ArrayList<ResultItem?>?) {
//                selectDepartmentList = ArrayList()
//                selectDepartmentList = departmentList
////                getDepartmentString=""
////                getDepartmentIdString=""
////                for (i in 0 until departmentList?.size!!) {
////                    if (departmentList?.get(i)?.isChecked.equals("true")){
////                        if (i==0){
////                            getDepartmentString=departmentList?.get(i)?.title+","
////                            getDepartmentIdString=departmentList?.get(i)?.id+","
////                        }else{
////                            getDepartmentString=getDepartmentString+departmentList?.get(i)?.title
////                            getDepartmentIdString=getDepartmentIdString+departmentList?.get(i)?.id
////                        }
////                    }
////
////
////                }
////                departmentArrayList=departmentList
////                dialogClickCallback.onConfirm(text,id)
////                dialog.dismiss()
////                getDepartmentString=text
////                getDepartmentIdString=id
//            }
////            override fun onConfirm(text: String) {
////                dialogClickCallback.onConfirm(text)
////                dialog.dismiss()
////            }
//        }
//
//
//
//        btn_dropdown_cancel?.setOnClickListener {
//            if (departmentList != null && departmentList.size > 0) {
//                for (item in departmentList) {
//                    item?.isChecked = "false"
//                }
//            }
//            dialog.dismiss()
//        }
//
//        btn_dropdown_ok?.setOnClickListener {
////            dialogClickCallback.onConfirm(getDepartmentString,getDepartmentIdString)
//            dialogClickCallback.onConfirm(selectDepartmentList)
//            dialog.dismiss()
//        }
//        dialog.show()
//    }

    fun showDialogForDepartmentDropDownList(
        context: Context,
        departmentList: ArrayList<HospitalResultItem?>?,
        title: String,
        dialogClickCallback: OnDepartmentDropDownListItemClickListener
    ) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_department_dropdown)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(false)
        val btn_dropdown_ok = dialog.findViewById<MyCustomButton>(R.id.btn_dropdown_ok)
        val btn_dropdown_cancel = dialog.findViewById<MyCustomButton>(R.id.btn_dropdown_cancel)
        val recyclerView_department_dropdown_list = dialog.findViewById<RecyclerView>(R.id.recyclerView_department_dropdown_list)
        val tv_title = dialog.findViewById<TextView>(R.id.txt_departmentheader_title)
        tv_title?.setText(title)
        var getDepartmentString = ""
        var getDepartmentIdString = ""
        var selectDepartmentList: ArrayList<HospitalResultItem?>? = null
        val gridLayoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
        recyclerView_department_dropdown_list.layoutManager = gridLayoutManager
        val dropdownListAdapter = AdapterDropdownDepartment(departmentList, context)
        recyclerView_department_dropdown_list.adapter = dropdownListAdapter
        dropdownListAdapter.recyclerViewItemClick = object : OnDepartmentDropDownListItemClickListener {
            override fun onConfirm(departmentList: ArrayList<HospitalResultItem?>?) {
                selectDepartmentList = ArrayList()
                selectDepartmentList = departmentList
            }
        }



        btn_dropdown_cancel?.setOnClickListener {
            if (departmentList != null && departmentList.size > 0) {
                for (item in departmentList) {
                    item?.isChecked = "false"
                }
            }
            dialog.dismiss()
        }

        btn_dropdown_ok?.setOnClickListener {
            dialogClickCallback.onConfirm(selectDepartmentList)
            dialog.dismiss()
        }
        dialog.show()
    }


    fun showDialogForSuccess(context: Context, title: String, dialogClickCallback: DialogClickCallback) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog_success)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val confirm = dialog.findViewById<Button>(R.id.btn_positive)
        val txt_msg = dialog.findViewById<TextView>(R.id.txt_msg)
        txt_msg.setText(title)
//        val tv_title=dialog.findViewById<TextView>(R.id.tv_title)
//        if (title.equals("")){
//            tv_title?.setText("Thank You for filling up admission form. You will shortly be informed about the further process. You will only be able to login to app once your admission is confirmed.")
//        }else{
//            tv_title?.setText(title)
//        }

//        cancel.setOnClickListener {
//            dialogClickCallback.onDismiss()
//            dialog.dismiss()
//        }
        confirm.setOnClickListener {
            dialogClickCallback.onConfirm()
            dialog.dismiss()
        }
        dialog.show()
    }

    // Show Image uploading progress
    var dialog: Dialog? = null

}
