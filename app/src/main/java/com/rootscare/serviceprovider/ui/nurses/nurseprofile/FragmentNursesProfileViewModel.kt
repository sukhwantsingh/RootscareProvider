package com.rootscare.serviceprovider.ui.nurses.nurseprofile

import android.util.Log
import com.google.gson.Gson
import com.rootscare.serviceprovider.ui.base.BaseViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FragmentNursesProfileViewModel : BaseViewModel<FragmentNursesProfileNavigator>() {
    fun apiDepartmentListing() {
        val disposable = apiServiceWithGsonFactory.hospDepartmentList()
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                   navigator.successDepartmentListResponse(response)
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiProfile(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.apiUserprofile(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    appSharedPref?.loginmodeldata = Gson().toJson(response)
                   navigator.onSuccessUserProfile(response)
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiHospitalDoctorProfile(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.apiHospitalDoctorProfile(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                   navigator.onSuccessUserProfile(response)
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun disableDoc(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.apiHospitalDoctorDisable(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                   navigator.onSuccessCommon(response)
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }
    fun deleteDoc(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.apiHospitalDoctorDelete(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                   navigator.onSuccessCommon(response)
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                    Log.d("check_response_error", ": " + throwable.message)
                }
            })

        compositeDisposable.add(disposable)
    }

    fun apiSpecialityList(reqBody: RequestBody) {
        val disposable = apiServiceWithGsonFactory.fetchSpeciality(reqBody)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (null != response) {
                    Log.d("check_response", ": $response")
                    navigator.onSuccessSpeciality(response)
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

    fun apiSubmitEditProfile(
        user_id: RequestBody?, uType: RequestBody?, name: RequestBody?, email: RequestBody?, mobNum: RequestBody?, dob: RequestBody?,
        gender: RequestBody?, qualification: RequestBody? = null, idnNumber: RequestBody? = null, speciality: RequestBody? = null,
        exp: RequestBody?,  scfhsNumber: RequestBody?, desc: RequestBody?, uImage: MultipartBody.Part? = null,  idImg: MultipartBody.Part? = null,
        qualiImg: MultipartBody.Part? = null, scfhs: MultipartBody.Part? = null, mohLicNum: RequestBody? = null,
        hospRegNum: RequestBody? = null, hospAddress: RequestBody? = null,
        departments: RequestBody? = null, workArea: RequestBody? = null) {

        val disposable = apiServiceWithGsonFactory.apiEditProfileForProviders(user_id,uType,name,email,
            mobNum, dob,gender,idnNumber, speciality,qualification, exp,scfhsNumber,desc,uImage,
            idImg,qualiImg,scfhs, mohLicNum, hospRegNum, hospAddress, departments,workArea)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    appSharedPref?.loginmodeldata = Gson().toJson(response)
                    navigator.onSuccessEditProfile(response)
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

    fun apiCreateDoc(idImg: MultipartBody.Part? = null,
                  qlImg: MultipartBody.Part? = null,
                  scfsImg: MultipartBody.Part? = null,
                  vararg allparams: RequestBody) {
        val disposable = apiServiceWithGsonFactory.apiCreateHospitalDoctor(
            allparams[0], allparams[1], allparams[2], allparams[3], allparams[4],
            allparams[5], allparams[6], allparams[6], allparams[7],
            allparams[8], allparams[9], allparams[10], allparams[11],
            idImg, qlImg, scfsImg, allparams[12], allparams[13])
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    navigator.successRegistrationResponse(response)
                }
            }, { throwable ->
                run {
                    navigator.errorInApi(throwable)
                }
            })
        compositeDisposable.add(disposable)
    }



    fun apiEditHospitalDoctorProfile(
        workArea: RequestBody?, user_id: RequestBody?, uType: RequestBody?, name: RequestBody?, email: RequestBody?, mobNum: RequestBody?, dob: RequestBody?,
        gender: RequestBody?, qualification: RequestBody? = null, idnNumber: RequestBody? = null, speciality: RequestBody? = null,
        exp: RequestBody?,  scfhsNumber: RequestBody?, desc: RequestBody?, uImage: MultipartBody.Part? = null,  idImg: MultipartBody.Part? = null,
        qualiImg: MultipartBody.Part? = null, scfhs: MultipartBody.Part? = null) {

        val disposable = apiServiceWithGsonFactory.apiEditProfileHospitalDoctor(workArea,user_id, uType, name, email,mobNum, dob,gender,idnNumber,
            speciality,qualification, exp,scfhsNumber,desc,uImage,
            idImg,qualiImg,scfhs)
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (response != null) {
                    navigator.onSuccessEditProfile(response)
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

    fun apiServiceFor(reqBody: RequestBody? = null) {
        val disposable = apiServiceWithGsonFactory.apiServiceFor()
            .subscribeOn(_scheduler_io)
            .observeOn(_scheduler_ui)
            .subscribe({ response ->
                if (null != response) {
                    Log.d("check_response", ": $response")
                    navigator.onSuccessServiceFor(response)
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
}