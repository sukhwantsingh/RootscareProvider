package com.rootscare.interfaces

import com.rootscare.data.model.response.hospital.HospitalResultItem

interface OnDepartmentDropDownListItemClickListener {
    fun onConfirm(departmentList: ArrayList<HospitalResultItem?>?)
}