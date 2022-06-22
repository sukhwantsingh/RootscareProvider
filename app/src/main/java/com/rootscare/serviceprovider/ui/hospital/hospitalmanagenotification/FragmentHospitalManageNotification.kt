package com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.dialog.CommonDialog
import com.rootscare.data.model.request.commonuseridrequest.CommonNotificationIdRequest
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.response.hospital.NotificationItemResult
import com.rootscare.data.model.response.hospital.NotificationResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentHospitalManageNotificationBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.adapter.AdapterHospitalManageNotificationRecyclerview
import com.rootscare.serviceprovider.ui.hospital.hospitalmanagenotification.adapter.OnNotificationCallback
import com.rootscare.serviceprovider.ui.notificationss.ModelUpdateRead
import com.rootscare.serviceprovider.ui.nurses.home.NursrsHomeActivity
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE

class FragmentHospitalManageNotification :
    BaseFragment<FragmentHospitalManageNotificationBinding, FragmentHospitalManageNotificationViewModel>(),
    FragmentHospitalManageNotificationNavigator {
    private var fragmentHospitalManageNotificationBinding: FragmentHospitalManageNotificationBinding? = null
    private var fragmentHospitalManageNotificationViewModel: FragmentHospitalManageNotificationViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_hospital_manage_notification
    override val viewModel: FragmentHospitalManageNotificationViewModel
        get() {
            fragmentHospitalManageNotificationViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalManageNotificationViewModel::class.java
            )
            return fragmentHospitalManageNotificationViewModel as FragmentHospitalManageNotificationViewModel
        }
    var mAdapterNotification: AdapterHospitalManageNotificationRecyclerview? = null
    companion object {
        fun newInstance(): FragmentHospitalManageNotification {
            val args = Bundle()
            val fragment = FragmentHospitalManageNotification()
            fragment.arguments = args
            return fragment
        }
    }

    var clickedPos = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalManageNotificationViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentHospitalManageNotificationBinding = viewDataBinding
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val commonUserIdRequest = CommonUserIdRequest()
            commonUserIdRequest.id = fragmentHospitalManageNotificationViewModel?.appSharedPref?.loginUserId

            fragmentHospitalManageNotificationViewModel?.apiGetAllUserNotifications(commonUserIdRequest)

        } else {
           showToast(getString(R.string.check_network_connection))
        }

    }


    fun updateReadNotiApi(id_:String){
        if (isNetworkConnected) {
           // baseActivity?.showLoading()
            val commonUserIdRequest = CommonNotificationIdRequest()
            commonUserIdRequest.id = id_
            commonUserIdRequest.read ="1"
            fragmentHospitalManageNotificationViewModel?.apiUpdateRead(commonUserIdRequest)

        } else {
            showToast(getString(R.string.network_unavailable))
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpPaymentHistoryListingRecyclerview(notificationItemResult: ArrayList<NotificationItemResult?>?) {
//        assert(fragmentPaymentHistoryBinding!!.recyclerViewRootsCarePaymentHistory != null)
        val recyclerView = fragmentHospitalManageNotificationBinding!!.recyclerViewHospitalManageNotification
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        mAdapterNotification= AdapterHospitalManageNotificationRecyclerview(notificationItemResult, requireContext())
        recyclerView.adapter = mAdapterNotification
        mAdapterNotification?.mCalllback = object :OnNotificationCallback{
            override fun onNotificationClick(node: NotificationItemResult?, pos:Int) {
                clickedPos = pos
                CommonDialog.showDialogForSingleButton(requireActivity(), object : DialogClickCallback {
                   }, getString(R.string.notification), node?.body?:"")

                if(!node?.read.equals("1",ignoreCase = true)) updateReadNotiApi(node?.id?:"")
            }
        }
    }

    override fun successNotificationListResponse(notificationResponse: NotificationResponse?) {
        baseActivity?.hideLoading()
        if (notificationResponse?.code.equals("200")) {
            if (notificationResponse?.result != null && notificationResponse.result.isNotEmpty()) {
                setUpPaymentHistoryListingRecyclerview(notificationResponse.result)

            } else {
                fragmentHospitalManageNotificationBinding?.recyclerViewHospitalManageNotification?.visibility =
                    View.GONE
                fragmentHospitalManageNotificationBinding?.tvNoDate?.visibility = View.VISIBLE
                fragmentHospitalManageNotificationBinding?.tvNoDate?.text = notificationResponse?.message
            }

        } else {
            fragmentHospitalManageNotificationBinding?.recyclerViewHospitalManageNotification?.visibility =
                View.GONE
            fragmentHospitalManageNotificationBinding?.tvNoDate?.visibility = View.VISIBLE
            fragmentHospitalManageNotificationBinding?.tvNoDate?.text = notificationResponse?.message
        }
    }

    override fun successUpdateRead(response: ModelUpdateRead?) {
        baseActivity?.hideLoading()
        if (response?.code.equals(SUCCESS_CODE)) {
            NursrsHomeActivity.isNotificationChecked.value = true
            if(clickedPos!= -1) mAdapterNotification?.updatedToRead(clickedPos)
        }
    }


    override fun errorPatientPaymentHistoryResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
          showToast(getString(R.string.something_went_wrong))
        }
    }

}