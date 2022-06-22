package com.rootscare.serviceprovider.ui.hospital.hospitalsamplecollection.subfragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.rootscare.data.model.request.hospital.getpathodetails
import com.rootscare.data.model.response.doctor.profileresponse.ReviewRatingItem
import com.rootscare.data.model.response.hospital.OrderDetails
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentSampleCpllectionDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls

class FragmentHospitalSampleCollectionDetails :
    BaseFragment<FragmentSampleCpllectionDetailsBinding, FragmentHospitalSampleCollectionDetailsViewModel>(),
    FragmentHospitalSampleCollectionDetailsnavigator {
    private var fragmentSampleCpllectionDetailsBinding: FragmentSampleCpllectionDetailsBinding? = null
    private var fragmentHospitalSampleCollectionDetailsViewModel: FragmentHospitalSampleCollectionDetailsViewModel? = null
    private var id_order: String? = null
    var doctorFirstName = ""
    var doctorLastName = ""
    var doctorEmail = ""
    var to_time = ""
    var from_time = ""
    var initialReviewRatingList: ArrayList<ReviewRatingItem?>? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_sample_cpllection_details
    override val viewModel: FragmentHospitalSampleCollectionDetailsViewModel
        get() {
            fragmentHospitalSampleCollectionDetailsViewModel = ViewModelProviders.of(this).get(
                FragmentHospitalSampleCollectionDetailsViewModel::class.java
            )
            return fragmentHospitalSampleCollectionDetailsViewModel as FragmentHospitalSampleCollectionDetailsViewModel
        }

    companion object {
        fun newInstance(id: String): FragmentHospitalSampleCollectionDetails {
            val args = Bundle()
            args.putString("id", id)
            val fragment = FragmentHospitalSampleCollectionDetails()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentHospitalSampleCollectionDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentSampleCpllectionDetailsBinding = viewDataBinding
        id_order = arguments?.getString("id")
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val commonUserIdRequest = getpathodetails()
            commonUserIdRequest.order_id = id_order
            fragmentHospitalSampleCollectionDetailsViewModel!!.apidoctorprofile(commonUserIdRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }


    }

    override fun successGetDoctorProfileResponse(getDoctorProfileResponse: OrderDetails?) {
        baseActivity?.hideLoading()
        if (getDoctorProfileResponse?.code.equals("200")) {
            if (getDoctorProfileResponse?.result != null) {

                doctorFirstName =
                    if (getDoctorProfileResponse.result.first_name != null && getDoctorProfileResponse.result.first_name != "") {
                        getDoctorProfileResponse.result.first_name
                    } else {
                        ""
                    }

                doctorLastName = if (getDoctorProfileResponse.result.last_name != null && getDoctorProfileResponse.result.last_name != "") {
                    getDoctorProfileResponse.result.last_name
                } else {
                    ""
                }
                fragmentSampleCpllectionDetailsBinding?.name?.text = "$doctorFirstName $doctorLastName"
                doctorEmail = if (getDoctorProfileResponse.result.order_id != null && getDoctorProfileResponse.result.order_id != "") {
                    getDoctorProfileResponse.result.order_id
                } else {
                    ""
                }

                fragmentSampleCpllectionDetailsBinding?.orderId?.text = doctorEmail

                val options: RequestOptions =
                    RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.profile_no_image)
                        .priority(Priority.HIGH)
                Glide
                    .with(this@FragmentHospitalSampleCollectionDetails)
                    .load(BaseMediaUrls.USERIMAGE.url + getDoctorProfileResponse.result.image)
                    .apply(options)
                    .into(fragmentSampleCpllectionDetailsBinding?.priscription!!)


                if (getDoctorProfileResponse.result.age != null && getDoctorProfileResponse.result.age != "") {
                    fragmentSampleCpllectionDetailsBinding?.years?.text = getDoctorProfileResponse.result.age
                } else {
                    fragmentSampleCpllectionDetailsBinding?.years?.text = ""
                }

                if (getDoctorProfileResponse.result.email != null && getDoctorProfileResponse.result.email != "") {
                    fragmentSampleCpllectionDetailsBinding?.address?.text = getDoctorProfileResponse.result.email
                } else {
                    fragmentSampleCpllectionDetailsBinding?.address?.text = ""
                }
                if (getDoctorProfileResponse.result.appointment_date != null && getDoctorProfileResponse.result.appointment_date != "") {
                    fragmentSampleCpllectionDetailsBinding?.appointentDate?.append(getDoctorProfileResponse.result.appointment_date)
                }
                if (getDoctorProfileResponse.result.phone_number != null && getDoctorProfileResponse.result.phone_number != "") {
                    fragmentSampleCpllectionDetailsBinding?.contactNumber?.append(getDoctorProfileResponse.result.phone_number)
                }
                if (getDoctorProfileResponse.result.amount != null && getDoctorProfileResponse.result.amount != "") {
                    fragmentSampleCpllectionDetailsBinding?.fee?.append(getDoctorProfileResponse.result.amount)
                }
                from_time = if (getDoctorProfileResponse.result.from_time != null && getDoctorProfileResponse.result.from_time != "") {
                    getDoctorProfileResponse.result.from_time
                } else {
                    ""
                }

                to_time = if (getDoctorProfileResponse.result.to_time != null && getDoctorProfileResponse.result.to_time != "") {
                    getDoctorProfileResponse.result.to_time
                } else {
                    ""
                }

                fragmentSampleCpllectionDetailsBinding?.apointmentTime?.text = "$from_time- $to_time"

                initialReviewRatingList = ArrayList()
/*
                if (getDoctorProfileResponse?.result?.reviewRating != null && getDoctorProfileResponse?.result?.reviewRating.size > 0) {
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorReview?.visibility = View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorReview?.visibility = View.GONE
                    finalReviewRatingList = getDoctorProfileResponse?.result?.reviewRating
                }
*/


            } else {
                Toast.makeText(activity, getDoctorProfileResponse?.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity, getDoctorProfileResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorInApi(throwable: Throwable?) {
    }

}