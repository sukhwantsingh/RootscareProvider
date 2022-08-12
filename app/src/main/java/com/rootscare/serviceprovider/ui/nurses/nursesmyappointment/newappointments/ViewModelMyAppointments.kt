package com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments

import android.util.Log
import com.google.gson.Gson
import com.rootscare.data.model.request.videoPushRequest.VideoPushRequest
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import com.rootscare.serviceprovider.ui.nurses.nursesmyappointment.newappointments.AppointmentDetailScreen.Companion.appointmentId
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ViewModelMyAppointments : BaseViewModel<AppointmentNavigator>() {
    fun apiOngoing(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.apiAppointmentOngoing(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessResponse(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
    fun apiPending(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.apiAppointmentPending(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessResponse(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
    fun apiUpcoming(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.apiAppointmentUpcoming(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessResponse(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
    fun apiPast(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.apiAppointmentPast(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onSuccessResponse(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

   fun apiMarkAs(reqBody: RequestBody, position: Int) {
  //  fun apiMarkAs(serv_type: RequestBody?, id:RequestBody?, acepStatus:RequestBody?, position: Int) {
   //     val disposable = apiServiceWithGsonFactory.apiMarkAcptRej2(serv_type,id,acepStatus)
        val disposable = apiServiceWithGsonFactory.apiMarkAcptRej(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onMarkAcceptReject(response, position)

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

     fun apiMarkAsComplete(reqBody: RequestBody) {
  //  fun apiMarkAsComplete(serv_type: RequestBody?, id:RequestBody?, otp_:RequestBody?,) {
     //   val disposable = apiServiceWithGsonFactory.apiMarkAsCompleted2(serv_type,id,otp_)
        val disposable = apiServiceWithGsonFactory.apiMarkAsCompleted(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.onMarkComplete(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiAppointmentDetails(reqBody: RequestBody) {
//    fun apiAppointmentDetails(serv_type: RequestBody?,id:RequestBody?) {
//        val disposable = apiServiceWithGsonFactory.getAppointmentDetails2(serv_type,id)
        val disposable = apiServiceWithGsonFactory.getAppointmentDetails(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                 //   Log.d("check_response", ": " + Gson().toJson(response))
                  navigator.onAppointmentDetail(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiUploadPrescription(appointmentId: RequestBody?, upload_prescription: MultipartBody.Part? = null) {
        val disposable = apiServiceWithGsonFactory.apiUploadPrescription(appointmentId, upload_prescription)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
            if (response != null) { navigator.onPrescriptionUploaded(response) }
            }, { throwable ->  run { navigator.errorInApi(throwable) }
            })
        compositeDisposable.add(disposable)
    }

    fun apiUploadLabReports(appointmentId: RequestBody?, patientId: RequestBody?, hospId: RequestBody?, repoList: ArrayList<MultipartBody.Part?>) {
        val disposable = apiServiceWithGsonFactory.apiUploadLabReports(appointmentId,patientId,hospId, repoList)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
            if (response != null) { navigator.onPrescriptionUploaded(response) }
            }, { throwable ->  run { navigator.errorInApi(throwable) }
            })
        compositeDisposable.add(disposable)
    }

    fun apiSendVideoPushNotification(videoPushRequest: VideoPushRequest) {
//        val body = RequestBody.create(MediaType.parse("application/json"), "")
        val disposable = apiServiceWithGsonFactory.sendVideoPushNotification(videoPushRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successVideoPushResponse(response)
                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
/*

    fun apiSendPush(pushNotificationRequest: PushNotificationRequest) {
        val disposable = apiServiceWithGsonFactory.sendPushNotification(pushNotificationRequest)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    // Store last login time
                    Log.d("check_response", ": " + Gson().toJson(response))
                    navigator.successPushNotification(response)

                } else {
                    Log.d("check_response", ": null response")
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

*/

}