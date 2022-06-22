package com.rootscare.serviceprovider.ui.caregiver.caregiverprofile

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
import com.rootscare.data.model.response.caregiver.profileresponse.GetDoctorProfileResponse
import com.rootscare.data.model.response.caregiver.profileresponse.QualificationDataItem
import com.rootscare.data.model.response.caregiver.profileresponse.ReviewRatingItem
import com.rootscare.data.model.response.doctor.profileresponse.DepartmentItem
import com.rootscare.interfaces.OnItemClickWithReportIdListener
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesProfileBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.adapter.AdapterCaregiverImportantDocumentrecyclerview
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.adapter.AdapterCaregiverdetailsReviewListRecyclerview
import com.rootscare.serviceprovider.ui.caregiver.caregiverprofile.editcaregivereprofile.FragmentEditCaregiverUpdateProfile
import com.rootscare.serviceprovider.ui.caregiver.home.CaregiverHomeActivity
import com.rootscare.serviceprovider.ui.doctor.profile.adapter.AdapterDoctordetailsSpecilityListRecyclerview
import com.rootscare.serviceprovider.ui.doctor.profile.bankDetails.FragmentBankDetails
import com.rootscare.serviceprovider.ui.login.subfragment.login.FragmentLogin
import com.rootscare.serviceprovider.ui.showimagelarger.TransaprentPopUpActivityForImageShow
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls

class FragmentCaregiverUpdateProfile : BaseFragment<FragmentNursesProfileBinding, FragmentCregiverUpdateProfileViewModel>(),
    FragmentCaregiverProfileUpdateNavigator {
    private var fragmentNursesProfileBinding: FragmentNursesProfileBinding? = null
    private var fragmentNursesProfileViewModel: FragmentCregiverUpdateProfileViewModel? = null
    private var initialReviewRatingList: ArrayList<ReviewRatingItem?>? = null
    private var finalReviewRatingList: ArrayList<ReviewRatingItem?>? = null
    private var doctorFirstName = ""
    private var doctorLastName = ""
    private var doctorEmail = ""
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_profile
    override val viewModel: FragmentCregiverUpdateProfileViewModel
        get() {
            fragmentNursesProfileViewModel = ViewModelProviders.of(this).get(
                FragmentCregiverUpdateProfileViewModel::class.java
            )
            return fragmentNursesProfileViewModel as FragmentCregiverUpdateProfileViewModel
        }


    companion object {
        fun newInstance(): FragmentCaregiverUpdateProfile {
            val args = Bundle()
            val fragment = FragmentCaregiverUpdateProfile()
            fragment.arguments = args
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentNursesProfileViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentNursesProfileBinding = viewDataBinding
//        setUpViewPrescriptionlistingRecyclerview()

        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val commonUserIdRequest = CommonUserIdRequest()
            commonUserIdRequest.id = fragmentNursesProfileViewModel?.appSharedPref?.loginUserId
            fragmentNursesProfileViewModel!!.apicaregiverprofile(commonUserIdRequest)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
        }

        fragmentNursesProfileBinding?.btnDoctorEditProfile?.setOnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackStackAndOpen(
//                FragmentNursesEditProfile.newInstance())
                FragmentEditCaregiverUpdateProfile.newInstance("caregiver")
            )
        }

        fragmentNursesProfileBinding?.tvReviewratingReadmore?.setOnClickListener {

            if (fragmentNursesProfileBinding?.tvReviewratingReadmore?.text!! == "Read More") {
                if (finalReviewRatingList != null && finalReviewRatingList!!.size > 0) {
                    setReviewRatingListing(finalReviewRatingList)
                    fragmentNursesProfileBinding?.tvReviewratingReadmore?.text = "Read Less"
                }

            } else if (fragmentNursesProfileBinding?.tvReviewratingReadmore?.text!! == "Read Less") {
                if (initialReviewRatingList != null && initialReviewRatingList!!.size > 0) {
                    setReviewRatingListing(initialReviewRatingList)
                    fragmentNursesProfileBinding?.tvReviewratingReadmore?.text = "Read More"
                }
            }
        }

        fragmentNursesProfileBinding?.llBankDetails?.setOnClickListener {
            (activity as CaregiverHomeActivity).checkFragmentInBackStackAndOpen(
                FragmentBankDetails.newInstance()
            )

        }

    }


    // Set up recycler view for service listing if available
    private fun setUpViewPrescriptionListingRecyclerView(qualificationDataList: ArrayList<QualificationDataItem>?) {
//        assert(fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorimportentDocument != null)
        val recyclerView = fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorimportentDocument
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(false)
        val contactListAdapter = AdapterCaregiverImportantDocumentrecyclerview(qualificationDataList, requireActivity())
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
//        assert(fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorReview != null)
        val recyclerView = fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorReview
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(false)
        val contactListAdapter = AdapterCaregiverdetailsReviewListRecyclerview(reviewRatingList, requireContext())
        recyclerView.adapter = contactListAdapter
    }

    // Set up recycler view for service listing if available
    private fun setSpecialityDataListing(departmentList: ArrayList<DepartmentItem?>?) {
//        assert(fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorSpeciality != null)
        val recyclerView = fragmentNursesProfileBinding!!.recyclerViewRootscareDoctorSpecility
        val gridLayoutManager = GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.setHasFixedSize(false)
        val contactListAdapter = AdapterDoctordetailsSpecilityListRecyclerview(departmentList, requireContext())
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
                fragmentNursesProfileBinding?.tvDoctorName?.text = "$doctorFirstName $doctorLastName"
                doctorEmail = if (getDoctorProfileResponse.result.email != null && getDoctorProfileResponse.result.email != "") {
                    getDoctorProfileResponse.result.email
                } else {
                    ""
                }

                fragmentNursesProfileBinding?.tvDoctorEmail?.text = doctorEmail

                val options: RequestOptions =
                    RequestOptions()
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .placeholder(R.drawable.profile_no_image)
                        .priority(Priority.HIGH)
                Glide
                    .with(requireActivity())
                    .load(BaseMediaUrls.USERIMAGE.url + getDoctorProfileResponse.result.image)
                    .apply(options)
                    .into(fragmentNursesProfileBinding?.imgDoctorProfile!!)

                if (getDoctorProfileResponse.result.avgRating != null && getDoctorProfileResponse.result.avgRating != "") {
//                    fragmentNursesProfileBinding?.tvReviews?.text = getDoctorProfileResponse.result.avgRating + " " + "reviews"
                    fragmentNursesProfileBinding?.tvReviews?.text = "${getDoctorProfileResponse.result.reviewRating!!.size}" + " reviews"
                    fragmentNursesProfileBinding?.ratingBarteacherFeedback?.rating = getDoctorProfileResponse.result.avgRating.toFloat()
                }

                if (getDoctorProfileResponse.result.qualification != null && getDoctorProfileResponse.result.qualification != "") {
                    fragmentNursesProfileBinding?.tvDoctorQualification?.text = getDoctorProfileResponse.result.qualification
                } else {
                    fragmentNursesProfileBinding?.tvDoctorQualification?.text = ""
                }

                if (getDoctorProfileResponse.result.address != null && getDoctorProfileResponse.result.address != "") {
                    fragmentNursesProfileBinding?.tvDoctorAddress?.text = getDoctorProfileResponse.result.address
                } else {
                    fragmentNursesProfileBinding?.tvDoctorAddress?.text = ""
                }
                if (getDoctorProfileResponse.result.description != null && getDoctorProfileResponse.result.description != "") {
                    fragmentNursesProfileBinding?.tvDoctorAddress?.append("\n" + getDoctorProfileResponse.result.description)
                }
                if (getDoctorProfileResponse.result.experience != null && getDoctorProfileResponse.result.experience != "") {
                    fragmentNursesProfileBinding?.tvDoctorAddress?.append("\n\nExperience: " + getDoctorProfileResponse.result.experience + " Years")
                }
                if (getDoctorProfileResponse.result.dailyRate != null && getDoctorProfileResponse.result.dailyRate != "") {
                    fragmentNursesProfileBinding?.tvDoctorFees?.text = "SAR" + " " + getDoctorProfileResponse.result.dailyRate
                } else {
                    fragmentNursesProfileBinding?.tvDoctorFees?.text = ""
                }

                if (getDoctorProfileResponse.result.qualificationData != null && getDoctorProfileResponse.result.qualificationData.size > 0) {
                    fragmentNursesProfileBinding?.recyclerViewRootscareDoctorimportentDocument?.visibility = View.VISIBLE
                    fragmentNursesProfileBinding?.tvNoDate?.visibility = View.GONE
                    setUpViewPrescriptionListingRecyclerView(getDoctorProfileResponse.result.qualificationData)
                } else {
                    fragmentNursesProfileBinding?.recyclerViewRootscareDoctorimportentDocument?.visibility = View.GONE
                    fragmentNursesProfileBinding?.tvNoDate?.visibility = View.VISIBLE
                    fragmentNursesProfileBinding?.tvNoDate?.text = "No important document found!"
                }
                if (getDoctorProfileResponse.result.experience != null && getDoctorProfileResponse.result.experience != "") {
                    fragmentNursesProfileBinding?.tvDoctorAddress?.append("\n\nExperience: " + getDoctorProfileResponse.result.experience + " Years")
                }
                if (getDoctorProfileResponse.result.department != null && getDoctorProfileResponse.result.department == "") {
                    /* fragmentNursesProfileBinding?.recyclerViewRootscareDoctorSpecility?.visibility=View.VISIBLE
                     fragmentNursesProfileBinding?.tvNoDataDoctorSpecility?.visibility=View.GONE
                     setSpecilityDataListing(getDoctorProfileResponse?.result?.department)*/


                } else {
                    /*  fragmentNursesProfileBinding?.recyclerViewRootscareDoctorSpecility?.visibility=View.GONE
                      fragmentNursesProfileBinding?.tvNoDataDoctorSpecility?.visibility=View.VISIBLE*/
                    fragmentNursesProfileBinding?.tvNoDataDoctorSpecility?.text = "No speciality found!"

                }
                initialReviewRatingList = ArrayList()
                finalReviewRatingList = ArrayList()

                if (getDoctorProfileResponse.result.reviewRating != null && getDoctorProfileResponse.result.reviewRating.size > 0) {
                    fragmentNursesProfileBinding?.recyclerViewRootscareDoctorReview?.visibility = View.VISIBLE
                    fragmentNursesProfileBinding?.tvNoDataDoctorReview?.visibility = View.GONE
                    finalReviewRatingList = getDoctorProfileResponse.result.reviewRating

                    if (getDoctorProfileResponse.result.reviewRating.size > 1) {
                        fragmentNursesProfileBinding?.tvReviewratingReadmore?.visibility = View.VISIBLE
                        val reviewRatingItem = ReviewRatingItem()
                        reviewRatingItem.rating = getDoctorProfileResponse.result.reviewRating[0]?.rating
                        reviewRatingItem.review = getDoctorProfileResponse.result.reviewRating[0]?.review
                        reviewRatingItem.reviewBy = getDoctorProfileResponse.result.reviewRating[0]?.reviewBy
                        initialReviewRatingList?.add(reviewRatingItem)
                        setReviewRatingListing(initialReviewRatingList)
                    } else {

                        fragmentNursesProfileBinding?.tvReviewratingReadmore?.visibility = View.GONE
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
                    fragmentNursesProfileBinding?.recyclerViewRootscareDoctorReview?.visibility = View.GONE
                    fragmentNursesProfileBinding?.tvNoDataDoctorReview?.visibility = View.VISIBLE
                    fragmentNursesProfileBinding?.tvNoDataDoctorReview?.text = "No review found"
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