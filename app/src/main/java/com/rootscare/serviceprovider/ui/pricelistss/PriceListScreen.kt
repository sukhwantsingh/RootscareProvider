package com.rootscare.serviceprovider.ui.pricelistss

import android.graphics.Color
import android.os.Bundle
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.ActivityPriceListScreenBinding
import com.rootscare.serviceprovider.ui.base.BaseActivity
import com.rootscare.serviceprovider.ui.nurses.nurseprofile.models.ModelUserProfile
import com.rootscare.serviceprovider.ui.pricelistss.fragments.FragmentPriceListCommon
import com.rootscare.serviceprovider.utilitycommon.LoginTypes
import com.rootscare.serviceprovider.utilitycommon.PriceTypes
import com.rootscare.serviceprovider.utilitycommon.getModelFromPref
import com.rootscare.utils.commonadapters.ViewPagerAdapterForTab
import kotlinx.coroutines.delay

class PriceListScreen : BaseActivity<ActivityPriceListScreenBinding, ManagePriceViewModel>() {
    private var binding: ActivityPriceListScreenBinding? = null
    private var mViewModel: ManagePriceViewModel? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.activity_price_list_screen
    override val viewModel: ManagePriceViewModel
        get() {
            mViewModel = ViewModelProviders.of(this).get(ManagePriceViewModel::class.java)
            return mViewModel as ManagePriceViewModel
        }
    lateinit var adapter: ViewPagerAdapterForTab

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = viewDataBinding
        binding?.topToolbar?.run {
            tvHeader.text = getString(R.string.price_list)
            btnBack.setOnClickListener { finish() }
        }

        lifecycleScope.launchWhenCreated {
            try {
                delay(5)
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
                        ContextCompat.getColor(this@PriceListScreen, R.color.color_tab_text_normal),
                        ContextCompat.getColor(this@PriceListScreen, R.color.color_tab_text_selected)
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
        when {
            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.NURSE.type) -> {
                adapter.addFragment(FragmentPriceListCommon.newInstance(PriceTypes.TASK_BASED.get()), PriceTypes.TASK_BASED.get())
                adapter.addFragment(FragmentPriceListCommon.newInstance(PriceTypes.HOURLY_BASED.get()), PriceTypes.HOURLY_BASED.get())
                binding?.tabLayout?.layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.BABYSITTER.type) ||
            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.CAREGIVER.type) -> {
                adapter.addFragment(FragmentPriceListCommon.newInstance(PriceTypes.HOURLY_BASED.get()), PriceTypes.HOURLY_BASED.get())
            }

            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.PHYSIOTHERAPY.type) -> {
                adapter.addFragment(FragmentPriceListCommon.newInstance(PriceTypes.TASK_BASED.get()), PriceTypes.TASK_BASED.get())
            }
            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.DOCTOR.type) -> {
             val mRes = viewModel.appSharedPref?.loginmodeldata?.getModelFromPref<ModelUserProfile>()?.result
                if(mRes?.hospital_id.isNullOrBlank()){
                    adapter.addFragment(FragmentPriceListCommon.newInstance(PriceTypes.ONLINE.get()), PriceTypes.ONLINE.get())
                    adapter.addFragment(FragmentPriceListCommon.newInstance(PriceTypes.HOME_VISIT.get()), PriceTypes.HOME_VISIT.get())
                    binding?.tabLayout?.layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
                    binding?.tabLayout?.tabMode = TabLayout.MODE_SCROLLABLE
                } else {
                    adapter.addFragment(FragmentPriceListCommon.newInstance(PriceTypes.ONLINE.get()), PriceTypes.ONLINE.get())
                }



            }
//            // Optional when doctor will do then will do accordingly
//            viewModel.appSharedPref?.loginUserType.equals(LoginTypes.DOCTOR.type) -> {
//                adapter.addFragment(FragmentPriceListCommon.newInstance(PriceTypes.HOURLY_BASED.get()), PriceTypes.HOURLY_BASED.get())
//            }

        }

    }


}