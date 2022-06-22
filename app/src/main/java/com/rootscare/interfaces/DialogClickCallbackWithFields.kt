package com.rootscare.interfaces

interface DialogClickCallbackWithFields {
    fun onConfirm(hospitalName: String? = null, address: String? = null)

    fun onDismiss()

}