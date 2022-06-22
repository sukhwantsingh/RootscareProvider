package com.rootscare.serviceprovider.ui.doctor.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.response.doctor.profileresponse.DepartmentItem
import com.rootscare.data.model.response.doctor.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.response.doctor.profileresponse.QualificationDataItem
import com.rootscare.data.model.response.doctor.profileresponse.ReviewRatingItem
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctorImportantDocumentrecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctordetailsReviewListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctordetailsSpecilityListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.bankDetails.FragmentBankDetails
import com.rootscare.serviceprovider.ui.doctor.profile.editdoctoreprofile.FragmentEditDoctorProfile
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.showimagelarger.TransaprentPopUpActivityForImageShow
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls

class FragmentDoctorProfile : BaseFragment<FragmentDoctorProfileBinding, FragmentDoctorProfileViewModel>(),
    FragmentDoctorProfileNavigator {
    private var fragmentDoctorProfileBinding: FragmentDoctorProfileBinding? = null
    private var fragmentDoctorProfileViewModel: FragmentDoctorProfileViewModel? = null
    var initialReviewRatingList: ArrayList<ReviewRatingItem?>? = null
    var finalReviewRatingList: ArrayList<ReviewRatingItem?>? = null
    var doctorFirstName = ""
    var doctorLastName = ""
    var doctorEmail = ""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_profile
    override val viewModel: FragmentDoctorProfileViewModel
        get() {
            fragmentDoctorProfileViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorProfileViewModel::class.java
            )
            return fragmentDoctorProfileViewModel as FragmentDoctorProfileViewModel
        }

    companion object {
        fun newInstance(): FragmentDoctorProfile {
            val args = Bundle()
            val fragment = FragmentDoctorProfile()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorProfileBinding = viewDataBinding
        fragmentDoctorProfileBinding?.btnDoctorEditProfile?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentEditDoctorProfile.newInstance("doctor", fragmentDoctorProfileViewModel?.appSharedPref?.loginUserId!!)
            )
        }
        // GET PROFILE API CALL
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val commonUserIdRequest = CommonUserIdRequest()
            commonUserIdRequest.id = fragmentDoctorProfileViewModel?.appSharedPref?.loginUserId
            fragmentDoctorProfileViewModel!!.apidoctorprofile(commonUserIdRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }

        //Add Read More Click
        fragmentDoctorProfileBinding?.tvReviewratingReadmore?.setOnClickListener {

            if (fragmentDoctorProfileBinding?.tvReviewratingReadmore?.text!! == "Read More") {
                if (finalReviewRatingList != null && finalReviewRatingList!!.size > 0) {
                    setReviewRatingListing(finalReviewRatingList)
                    fragmentDoctorProfileBinding?.tvReviewratingReadmore?.text = "Read Less"
                }

            } else if (fragmentDoctorProfileBinding?.tvReviewratingReadmore?.text!! == "Read Less") {
                if (initialReviewRatingList != null && initialReviewRatingList!!.size > 0) {
                    setReviewRatingListing(initialReviewRatingList)
                    fragmentDoctorProfileBinding?.tvReviewratingReadmore?.text = "Read More"
                }
            }
        }
        fragmentDoctorProfileBinding?.llBankDetails?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentBankDetails.newInstance()
            )

        }

    }

    // Set up recycler view for service listing if available
    private fun setUpViewPrescriptionListingRecyclerview(qualificationDataList: ArrayList<QualificationDataItem>?) {
        assert(fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorimportentDocument != null)
        val recyclerView = fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorimportentDocument
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(false)
        val contactListAdapter = AdapterDoctorImportantDocumentrecyclerview(qualificationDataList, activity!!)
        recyclerView.adapter = contactListAdapter

        contactListAdapter.recyclerViewItemClickWithView = object : OnItemClickWithReportIdListener {
            override fun onItemClick(id: Int) {
                val imageUrl =
                    BaseMediaUrls.USERIMAGE.url + contactListAdapter.qualificationDataList!![id].qualificationCertificate!!
                startActivity(TransaprentPopUpActivityForImageShow.newIntent(activity!!, imageUrl))
            }
        }
    }

    // Set up recycler view for service listing if available
    private fun setReviewRatingListing(reviewRatingList: ArrayList<ReviewRatingItem?>?) {
        val recyclerView = fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorReview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.isNestedScrollingEnabled = false
        val contactListAdapter = AdapterDoctordetailsReviewListRecyclerview(reviewRatingList, context!!)
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setSpecialityDataListing(departmentList: ArrayList<DepartmentItem?>?) {
        assert(fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorSpecility != null)
        val recyclerView = fragmentDoctorProfileBinding!!.recyclerViewRootscareDoctorSpecility
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(false)
        recyclerView.isNestedScrollingEnabled = false
        val contactListAdapter = AdapterDoctordetailsSpecilityListRecyclerview(departmentList, context!!)
        recyclerView.adapter = contactListAdapter
    }

    override fun successGetDoctorProfileResponse(getDoctorProfileResponse: GetDoctorProfileResponse?) {
        baseActivity?.hideLoading()
        if (getDoctorProfileResponse?.code.equals("200")) {
            if (getDoctorProfileResponse?.result != null) {

                doctorFirstName = if (getDoctorProfileResponse.result.firstName != null && getDoctorProfileResponse.result.firstName != "") {
                    getDoctorProfileResponse.result.firstName
                } else {
                    ""
                }

                doctorLastName = if (getDoctorProfileResponse.result.lastName != null && getDoctorProfileResponse.result.lastName != "") {
                    getDoctorProfileResponse.result.lastName
                } else {
                    ""
                }
                fragmentDoctorProfileBinding?.tvDoctorName?.text = "$doctorFirstName $doctorLastName"
                doctorEmail = if (getDoctorProfileResponse.result.email != null && getDoctorProfileResponse.result.email != "") {
                    getDoctorProfileResponse.result.email
                } else {
                    ""
                }

                fragmentDoctorProfileBinding?.tvDoctorEmail?.text = doctorEmail

                val options: RequestOptions =
                    RequestOptions()
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.profile_no_image)
                        .priority(Priority.HIGH)
                Glide
                    .with(this@FragmentDoctorProfile)
                    .load(BaseMediaUrls.USERIMAGE.url + getDoctorProfileResponse.result.image)
                    .apply(options)
                    .into(fragmentDoctorProfileBinding?.imgDoctorProfile!!)

                if (getDoctorProfileResponse.result.avgRating != null && getDoctorProfileResponse.result.avgRating != "") {
//                    fragmentDoctorProfileBinding?.tvReviews?.text = getDoctorProfileResponse.result.reviewRating!!.size!! $" " + "reviews"
//                    println("getDoctorProfileResponse.result.reviewRating!!.size ${getDoctorProfileResponse.result.reviewRating!!.size}")
                    fragmentDoctorProfileBinding?.tvReviews?.text = "${getDoctorProfileResponse.result.reviewRating!!.size}" + " reviews"
                    fragmentDoctorProfileBinding?.ratingBarteacherFeedback?.rating = getDoctorProfileResponse.result.avgRating.toFloat()
                }

                if (getDoctorProfileResponse.result.qualification != null && getDoctorProfileResponse.result.qualification != "") {
                    fragmentDoctorProfileBinding?.tvDoctorQualification?.text = getDoctorProfileResponse.result.qualification
                } else {
                    fragmentDoctorProfileBinding?.tvDoctorQualification?.text = ""
                }

                if (getDoctorProfileResponse.result.address != null && getDoctorProfileResponse.result.address != "") {
                    fragmentDoctorProfileBinding?.tvDoctorAddress?.text = getDoctorProfileResponse.result.address
                } else {
                    fragmentDoctorProfileBinding?.tvDoctorAddress?.text = ""
                }
                if (getDoctorProfileResponse.result.description != null && getDoctorProfileResponse.result.description != "") {
                    fragmentDoctorProfileBinding?.tvDoctorAddress?.append("\n" + getDoctorProfileResponse.result.description)
                }
                if (getDoctorProfileResponse.result.experience != null && getDoctorProfileResponse.result.experience != "") {
                    fragmentDoctorProfileBinding?.tvDoctorAddress?.append("\n\nExperience: " + getDoctorProfileResponse.result.experience + " Years")
                }
                if (getDoctorProfileResponse.result.fees != null && getDoctorProfileResponse.result.fees != "") {
                    fragmentDoctorProfileBinding?.tvDoctorFees?.text = "SAR" + " " + getDoctorProfileResponse.result.fees
                } else {
                    fragmentDoctorProfileBinding?.tvDoctorFees?.text = ""
                }

                if (getDoctorProfileResponse.result.qualificationData != null && getDoctorProfileResponse.result.qualificationData.size > 0) {
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorimportentDocument?.visibility = View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDate?.visibility = View.GONE
                    setUpViewPrescriptionListingRecyclerview(getDoctorProfileResponse.result.qualificationData)
                } else {
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorimportentDocument?.visibility = View.GONE
                    fragmentDoctorProfileBinding?.tvNoDate?.visibility = View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDate?.text = "No important document found!"
                }

                if (getDoctorProfileResponse.result.department != null && getDoctorProfileResponse.result.department.size > 0) {
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorSpecility?.visibility = View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorSpecility?.visibility = View.GONE
                    setSpecialityDataListing(getDoctorProfileResponse.result.department)
                } else {
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorSpecility?.visibility = View.GONE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorSpecility?.visibility = View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorSpecility?.text = "No speciality found!"

                }
                initialReviewRatingList = ArrayList()
                finalReviewRatingList = ArrayList()

                if (getDoctorProfileResponse.result.reviewRating != null && getDoctorProfileResponse.result.reviewRating.size > 0) {
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorReview?.visibility = View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorReview?.visibility = View.GONE
                    finalReviewRatingList = getDoctorProfileResponse.result.reviewRating

                    if (getDoctorProfileResponse.result.reviewRating.size > 1) {
                        fragmentDoctorProfileBinding?.tvReviewratingReadmore?.visibility = View.VISIBLE
                        val reviewRatingItem = ReviewRatingItem()
                        reviewRatingItem.rating = getDoctorProfileResponse.result.reviewRating[0]?.rating
                        reviewRatingItem.review = getDoctorProfileResponse.result.reviewRating[0]?.review
                        reviewRatingItem.reviewBy = getDoctorProfileResponse.result.reviewRating[0]?.reviewBy
                        initialReviewRatingList?.add(reviewRatingItem)
                        setReviewRatingListing(initialReviewRatingList)
                    } else {

                        fragmentDoctorProfileBinding?.tvReviewratingReadmore?.visibility = View.GONE
                        finalReviewRatingList = ArrayList()
                        for (i in 0 until getDoctorProfileResponse.result.reviewRating.size) {
                            val reviewRatingItem = ReviewRatingItem()
                            reviewRatingItem.rating = getDoctorProfileResponse.result.reviewRating[0]?.rating
                            reviewRatingItem.review = getDoctorProfileResponse.result.reviewRating[0]?.review
                            reviewRatingItem.reviewBy = getDoctorProfileResponse.result.reviewRating[0]?.reviewBy
                            finalReviewRatingList?.add(reviewRatingItem)
                            setReviewRatingListing(finalReviewRatingList)
                        }

                    }
                } else {
                    fragmentDoctorProfileBinding?.recyclerViewRootscareDoctorReview?.visibility = View.GONE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorReview?.visibility = View.VISIBLE
                    fragmentDoctorProfileBinding?.tvNoDataDoctorReview?.text = "No review found"
                }
            } else {
                Toast.makeText(activity, getDoctorProfileResponse?.message, Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity, getDoctorProfileResponse?.message, Toast.LENGTH_SHORT).show()
        }

    }

    override fun errorGetDoctorProfileResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(FragmentLogin.TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

}