package com.rootscare.serviceprovider.ui.transactionss

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityTransactionsMoreBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.transactionss.fragments.FragmentTransactionCommon
import com.rootscare.serviceprovider.ui.transactionss.fragments.FragmentWithdrawal
import com.rootscare.serviceprovider.utilitycommon.TransTypes
import com.rootscare.utils.commonadapters.ViewPagerAdapterForTab
import kotlinx.coroutines.delay

class TransactionsMore : BaseActivity<ActivityTransactionsMoreBinding, PaymentTransactionsViewModel>() {

    companion object {
        var isNeedToRefreshWithdrawable :Boolean? = null
    }
    private var binding: ActivityTransactionsMoreBinding? = null
    private var mViewModel: PaymentTransactionsViewModel? = null

    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_transactions_more
    override val viewModel: PaymentTransactionsViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(PaymentTransactionsViewModel::class.java)
            return mViewModel as PaymentTransactionsViewModel
        }
    lateinit var adapter: ViewPagerAdapterForTab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        binding?.topToolbar?.run {
            tvHeader.text = getString(R.string.transaction_and_more)
            btnBack.setOnClickListener { onBackPressed()}
        }

        lifecycleScope.launchWhenCreated {
            try {
                delay(2)
                setupViewPager(binding?.viewPager)
            } catch (e: Exception) {
                hideLoading()
                println("$e")
            }
        }
    }

    private var currentFragment: Fragment? = null
    private fun setupViewPager(viewPager: ViewPager?) {
        try {
            adapter = ViewPagerAdapterForTab(supportFragmentManager)
            setFragmentAccordingToUser()
            viewPager?.adapter = adapter
            try {
                binding?.run {
                    tabLayout.setupWithViewPager(binding?.viewPager)
                    viewPager?.offscreenPageLimit = 2
                    tabLayout.setTabTextColors(
                        ContextCompat.getColor(this@TransactionsMore, R.color.color_tab_text_normal),
                        ContextCompat.getColor(this@TransactionsMore, R.color.color_tab_text_selected)
                    )

                    tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#0888D1"))
                }
            } catch (e: Exception) {
                println("$e")
            }

            viewPager?.isSaveFromParentEnabled = false
        } catch (e: Exception) {
            println("$e")
        }
        try {
            viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrollStateChanged(state: Int) {

                }

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    try {
                        currentFragment = if (position == 0) {
                            adapter.getItem(0)
                        } else {
                            adapter.getItem(1)
                        }
                    } catch (e: Exception) {
                        println(e.message)
                    }
                }

                override fun onPageSelected(position: Int) {

                }

            })
        } catch (e: Exception) {
            println(e.message)
        }

    }

    private fun setFragmentAccordingToUser() {
     //   when {
         //   viewModel.appSharedPref?.loginUserType.equals(LoginTypes.NURSE.type) -> {
                adapter.addFragment(FragmentTransactionCommon.newInstance(), TransTypes.TRANSACTION.get())
                adapter.addFragment(FragmentWithdrawal.newInstance(), TransTypes.WITHDRAWAL.get())
                binding?.tabLayout?.layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        //    }
//            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.BABYSITTER.type) -> {
//                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.HOURLY_BASED.nm), ScheduleTypes.HOURLY_BASED.nm)
//            }
//            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.CAREGIVER.type) -> {
//                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.HOURLY_BASED.nm), ScheduleTypes.HOURLY_BASED.nm)
//            }
//            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.PHYSIOTHERAPY.type) -> {
//                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.TASK_BASED.nm), ScheduleTypes.TASK_BASED.nm)
//            }
//            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.LAB_TECHNICIAN.type) -> {
//                adapter.addFragment(ScheduleFragmentCombine.newInstance(ScheduleTypes.TASK_BASED.nm), "LAB Schedule")
//            }
//        }

    }

    override fun onStart() {
        super.onStart()
        isNeedToRefreshWithdrawable?.let {
            if(it) {
                isNeedToRefreshWithdrawable = false
                (currentFragment as? FragmentWithdrawal)?.refreshFragment()
            }
        }

    }

}