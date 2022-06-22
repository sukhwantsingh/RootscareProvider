package com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.dialog.CommonDialog
import com.rootscare.data.model.request.commonuseridrequest.NeedSupportRequest
import com.rootscare.data.model.response.doctor.review.ReviewResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentNursesReviewAndRatingBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating.adapter.AdapterNursesReviewAndRating
import com.rootscare.serviceprovider.ui.nurses.nursesreviewandrating.adapter.OnReviewClickCallback
import com.rootscare.serviceprovider.utilitycommon.SUCCESS_CODE

class FragmentNursesReviewAndRating : BaseFragment<FragmentNursesReviewAndRatingBinding, FragmentNursesReviewAndRatingViewModel>(),
    FragmentNursesReviewAndRatingNavigator {

    private var contactListAdapter: AdapterNursesReviewAndRating? = null

    private var mBinding: FragmentNursesReviewAndRatingBinding? = null
    private var mViewModel: FragmentNursesReviewAndRatingViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_nurses_review_and_rating
    override val viewModel: FragmentNursesReviewAndRatingViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(
                FragmentNursesReviewAndRatingViewModel::class.java
            )
            return mViewModel as FragmentNursesReviewAndRatingViewModel
        }

    companion object {
        fun newInstance(): FragmentNursesReviewAndRating {
            val args = Bundle()
            val fragment = FragmentNursesReviewAndRating()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel?.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding = viewDataBinding
        setUpViewReviewAndRatinglistingRecyclerview()
        fetchReviews()
    }

    private fun fetchReviews(){
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            val reqBody = NeedSupportRequest()
            reqBody.user_id = mViewModel?.appSharedPref?.loginUserId
            reqBody.service_type = mViewModel?.appSharedPref?.loginUserType

            mViewModel?.getReviewFromApi(reqBody)
        } else {
            showToast(getString(R.string.check_network_connection))
        }
    }

    // Set up recycler view for service listing if available
    private fun setUpViewReviewAndRatinglistingRecyclerview() {
//        trainerList: ArrayList<TrainerListItem?>?
        val recyclerView = mBinding?.recyclerViewNursesReviewandrating
        recyclerView?.setHasFixedSize(true)
        contactListAdapter = AdapterNursesReviewAndRating(requireContext())
        recyclerView?.adapter = contactListAdapter
        contactListAdapter?.mCallback= object : OnReviewClickCallback {
            override fun onReviewClick(reviewText: String) {
                CommonDialog.showDialogForSingleButton(requireActivity(), object : DialogClickCallback {
                }, getString(R.string.review), reviewText)
            }

        }
    }

    override fun onSuccessReview(response: ReviewResponse) {
        baseActivity?.hideLoading()
        if (response.code.equals(SUCCESS_CODE)) {
            if (response.result != null && response.result.size > 0) {
                mBinding?.recyclerViewNursesReviewandrating?.visibility = View.VISIBLE
                mBinding?.tvNoDate?.visibility = View.GONE

                contactListAdapter?.result = response.result
                contactListAdapter?.notifyDataSetChanged()
            } else {
                noData(response.message)
            }
        } else {
            noData(response.message)

        }
    }
fun noData(msg:String?){
    mBinding?.tvNoDate?.text = msg ?: getString(R.string.something_went_wrong)
    mBinding?.recyclerViewNursesReviewandrating?.visibility = View.GONE
    mBinding?.tvNoDate?.visibility = View.VISIBLE
}
    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
        noData(throwable.message)
    }
}