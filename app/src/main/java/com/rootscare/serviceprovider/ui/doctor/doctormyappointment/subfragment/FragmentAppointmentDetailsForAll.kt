package com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.request.RequestOptions
import com.dialog.CommonDialog
import com.rootscare.data.model.request.commonuseridrequest.CommonUserIdRequest
import com.rootscare.data.model.request.doctor.appointment.updateappointmentrequest.UpdateAppointmentRequest
import com.rootscare.data.model.request.videoPushRequest.VideoPushRequest
import com.rootscare.data.model.response.CommonResponse
import com.rootscare.data.model.response.doctor.appointment.appointmentdetails.AppointmentDetailsResponse
import com.rootscare.data.model.response.doctor.appointment.requestappointment.getrequestappointment.GetDoctorRequestAppointmentResponse
import com.rootscare.data.model.response.doctor.appointment.todaysappointment.ResultItem
import com.rootscare.data.model.response.videoPushResponse.VideoPushResponse
import com.rootscare.interfaces.DialogClickCallback
import com.rootscare.interfaces.OnClickOfDoctorAppointment
import com.rootscare.serviceprovider.BR
import com.rootscare.serviceprovider.R
import com.rootscare.serviceprovider.databinding.FragmentDoctorAppointmentDetailsBinding
import com.rootscare.serviceprovider.ui.base.BaseFragment
import com.rootscare.serviceprovider.ui.doctor.doctorconsulting.FragmentDoctorConsulting
import com.rootscare.serviceprovider.ui.doctor.doctormyappointment.subfragment.todaysappointment.tempModel.AddlabTestImageSelectionModel
import com.rootscare.serviceprovider.ui.home.HomeActivity
import com.rootscare.serviceprovider.ui.showimagelarger.TransaprentPopUpActivityForImageShow
import com.rootscare.serviceprovider.utilitycommon.BaseMediaUrls
import com.rootscare.twilio.VideoCallActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class FragmentAppointmentDetailsForAll : BaseFragment<FragmentDoctorAppointmentDetailsBinding, FragmentDoctorAppointmentDetailsViewModel>(),
    FragmentDoctorAppointmentDetailsNavigator {

    private var patientIdForPrescriptionUpload: String? = null
    private var doctorId: String? = null

    //    private var doctorId: String? = null
    private var imageSelectionModel: AddlabTestImageSelectionModel? = null
    private var appointmentId: String? = null
    private var serviceType: String? = null
    private var statusOfAppointmentForFlowChange: String? = null

    private var mediaPlayer: MediaPlayer? = null

    private var appointmentDetailsResponse: AppointmentDetailsResponse? = null

    private var fragmentDoctorAppointmentDetailsBinding: FragmentDoctorAppointmentDetailsBinding? = null
    private var fragmentDoctorAppointmentDetailsViewModel: FragmentDoctorAppointmentDetailsViewModel? = null
    private var roomName: String? = null
    override val bindingVariable: Int
        get() = BR.viewModel
    override val layoutId: Int
        get() = R.layout.fragment_doctor_appointment_details
    override val viewModel: FragmentDoctorAppointmentDetailsViewModel
        get() {
            fragmentDoctorAppointmentDetailsViewModel = ViewModelProviders.of(this).get(
                FragmentDoctorAppointmentDetailsViewModel::class.java
            )
            return fragmentDoctorAppointmentDetailsViewModel as FragmentDoctorAppointmentDetailsViewModel
        }

    companion object {
        private const val TAG = "FragmentAppointmentData"
        fun newInstance(appointmentId: String, service_type: String): FragmentAppointmentDetailsForAll {
            val args = Bundle()
            args.putString("appointmentId", appointmentId)
            args.putString("service_type", service_type)
            val fragment = FragmentAppointmentDetailsForAll()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(
            appointmentId: String,
            service_type: String,
            statusOfAppointmentForFlowChange: String
        ): FragmentAppointmentDetailsForAll {
            val args = Bundle()
            args.putString("appointmentId", appointmentId)
            args.putString("service_type", service_type)
            args.putString("statusOfAppointmentForFlowChange", statusOfAppointmentForFlowChange)
            val fragment = FragmentAppointmentDetailsForAll()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(
            todayAppointmentList: ResultItem?,
            service_type: String,
            statusOfAppointmentForFlowChange: String,
            appointmentId: String?
        ): FragmentAppointmentDetailsForAll {
            val args = Bundle()
            args.putSerializable("todayAppointmentList", todayAppointmentList)
            args.putString("service_type", service_type)
            args.putString("statusOfAppointmentForFlowChange", statusOfAppointmentForFlowChange)
            args.putString("appointmentId", appointmentId)
//            args.putString("patientIdForPrescriptionUploadFromTodayAppointment", patientIdForPrescriptionUploadFromTodayAppointment)
            val fragment = FragmentAppointmentDetailsForAll()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(
            appointmentId: String,
            service_type: String,
            statusOfAppointmentForFlowChange: String,
            patientIdForPrescriptionUploadFromTodayAppointment: String,
            doctorId: String?
        ): FragmentAppointmentDetailsForAll {
            val args = Bundle()
            args.putString("appointmentId", appointmentId)
            args.putString("service_type", service_type)
            args.putString("statusOfAppointmentForFlowChange", statusOfAppointmentForFlowChange)
            args.putString("patientIdForPrescriptionUploadFromTodayAppointment", patientIdForPrescriptionUploadFromTodayAppointment)
            args.putString("doctorId", doctorId)
            val fragment = FragmentAppointmentDetailsForAll()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentDoctorAppointmentDetailsViewModel!!.navigator = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentDoctorAppointmentDetailsBinding = viewDataBinding
        doctorId = arguments?.getString("doctorId")
        patientIdForPrescriptionUpload = arguments?.getString("patientIdForPrescriptionUploadFromTodayAppointment")
        statusOfAppointmentForFlowChange = arguments?.getString("statusOfAppointmentForFlowChange")
        appointmentId = arguments?.getString("appointmentId")
        serviceType = arguments?.getString("service_type")
        apiHitForDetails()

        fragmentDoctorAppointmentDetailsBinding?.btnRootscareDoctorConsulting?.setOnClickListener {
            (activity as HomeActivity).checkFragmentInBackStackAndOpen(
                FragmentDoctorConsulting.newInstance()
            )
        }
    }


    private fun apiHitForDetails() {
        val request = CommonUserIdRequest()
        request.id = appointmentId
        request.service_type = serviceType
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            fragmentDoctorAppointmentDetailsViewModel!!.getDetails(request)
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onSuccessDetails(response: AppointmentDetailsResponse) {
        appointmentDetailsResponse = response


        baseActivity?.hideLoading()
        if (response.code.equals("200")) {
            if (response.result != null) {

                with(fragmentDoctorAppointmentDetailsBinding!!) {
                    crdviewDoctorappoitmentList.visibility = View.VISIBLE
                    if (serviceType.equals("doctor")) {
                        if (response.result.doctorImage != null && !TextUtils.isEmpty(response.result.doctorImage.trim())) {
                            val options: RequestOptions =
                                RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.profile_no_image)
                                    .priority(Priority.HIGH)
                            Glide
                                .with(requireActivity())
                                .load(BaseMediaUrls.USERIMAGE.url + response.result.doctorImage)
                                .apply(options)
                                .into(imageView)
                        }
                    } else if (serviceType.equals("nurse")) {
                        if (response.result.nurseImage != null && !TextUtils.isEmpty(response.result.nurseImage.trim())) {
                            val options: RequestOptions =
                                RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.profile_no_image)
                                    .priority(Priority.HIGH)

                            Glide
                                .with(requireActivity())
                                .load(BaseMediaUrls.USERIMAGE.url + response.result.nurseImage)
                                .apply(options)
                                .into(imageView)
                        }
                    } else if (serviceType.equals("physiotherapy")) {
                        if (response.result.physiotherapistImage != null && !TextUtils.isEmpty(response.result.physiotherapistImage.trim())) {
                            val options: RequestOptions =
                                RequestOptions()
                                    .centerCrop()
                                    .placeholder(R.drawable.profile_no_image)
                                    .priority(Priority.HIGH)

                            Glide
                                .with(requireActivity())
                                .load(BaseMediaUrls.USERIMAGE.url + response.result.physiotherapistImage)
                                .apply(options)
                                .into(imageView)
                        }
                    }


                    if (serviceType.equals("doctor")) {
                        if (response.result.doctorName != null && !TextUtils.isEmpty(response.result.doctorName.trim())) {
                            tvDoctorName.text = response.result.doctorName
                        }
                    } else if (serviceType.equals("nurse")) {


                        if (response.result.taskDetails != null) {
                            tvAddress.text = response.result.taskDetails.test_name
                        }else{
                            tvAddress.text = "N/A"
                        }
                        if (response.result.nurseName != null && !TextUtils.isEmpty(response.result.nurseName.trim())) {
                            tvDoctorName.text = response.result.nurseName
                        }
                      tvAddressName.text = "Task Details : "



                    } else if (serviceType.equals("physiotherapy")) {

                        if (response.result.taskDetails != null) {
                            tvAddress.text = response.result.taskDetails.test_name
                        }else{
                            tvAddress.text = "N/A"
                        }


                        if (response.result.physiotherapistName != null && !TextUtils.isEmpty(response.result.physiotherapistName.trim())) {
                            tvDoctorName.text = response.result.physiotherapistName
                        }
                    }

                    if (response.result.patientName != null && !TextUtils.isEmpty(response.result.patientName.trim())) {
                        txtPatientName.text = response.result.patientName
                    }
                    if (patientIdForPrescriptionUpload == null) {
                        if (response.result.patient_id != null && !TextUtils.isEmpty(response.result.patient_id?.trim())) {
                            patientIdForPrescriptionUpload = response.result.patient_id
                        }
                    }
                    if (response.result.bookingDate != null && !TextUtils.isEmpty(response.result.bookingDate.trim())) {
                        txtBookingDate.text = formatDateFromString(
                            "yyyy-MM-dd hh:mm:ss", "dd MMM yyyy", response.result.bookingDate
                        )
                    }
                    if (response.result.patientContact != null && !TextUtils.isEmpty(response.result.patientContact.trim())) {
                        tvPhoneNumber.text = response.result.patientContact
                    }
                    if (response.result.appointmentDate != null && !TextUtils.isEmpty(response.result.appointmentDate.trim())) {
                        tvAppointmentDate.text = formatDateFromString(
                            "yyyy-MM-dd", "dd MMM yyyy", response.result.appointmentDate
                        )
                    }

                    if (response.result.fromTime != null && !TextUtils.isEmpty(response.result.fromTime.trim())) {
                        llAppointmentTime.visibility = View.VISIBLE
                        tvAppointmentTime.text = response.result.fromTime
                    }

                    if (response.result.appointmentStatus != null && !TextUtils.isEmpty(response.result.appointmentStatus.trim())) {
                        tvAppointmentStatus.text = response.result.appointmentStatus
                    }
                    if (response.result.acceptanceStatus != null && !TextUtils.isEmpty(response.result.acceptanceStatus.trim())) {
                        tvAcceptanceStatus.text = response.result.acceptanceStatus
                    }


                    if (response.result.symptomRecording != null && !TextUtils.isEmpty(response.result.symptomRecording.trim())) {
                        llRecording.visibility = View.VISIBLE
                        val audioUrl =BaseMediaUrls.USERIMAGE.url + response.result.symptomRecording
                        setUpAudioLayout(audioUrl)
                        if (response.result.symptomText != null && !TextUtils.isEmpty(response.result.symptomText.trim())) {
                            tvSymptoms.visibility = View.VISIBLE
                            llOrPortion.visibility = View.VISIBLE
                            tvSymptoms.text = "Symptoms: ${response.result.symptomText}"
                        } else {
                            tvSymptoms.visibility = View.GONE
                        }
                    } else {
                        llRecording.visibility = View.GONE
                        if (response.result.symptomText != null && !TextUtils.isEmpty(response.result.symptomText.trim())) {
                            tvSymptoms.visibility = View.VISIBLE
                            llOrPortion.visibility = View.GONE
                            tvSymptoms.text = "Symptoms: ${response.result.symptomText}"
                        } else {
                            tvSymptoms.visibility = View.GONE
                        }
                    }

                    if ((response.result.symptomRecording != null && !TextUtils.isEmpty(response.result.symptomRecording.trim())) ||
                        (response.result.symptomText != null && !TextUtils.isEmpty(response.result.symptomText.trim()))
                    ) {
                        llAboutSymptoms.visibility = View.VISIBLE
                    } else {
                        llAboutSymptoms.visibility = View.GONE
                    }

                    if (response.result.acceptanceStatus != null && !TextUtils.isEmpty(response.result.acceptanceStatus.trim())) {
                        tvAcceptanceStatus.text = response.result.acceptanceStatus
                    }

                    if (response.result.prescription != null && response.result.prescription.size > 0) {
                        llPrescription.visibility = View.VISIBLE
                        prescriptionRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        val prescriptionList = response.result.prescription
                        val adapter =
                            ShowPrescriptionsImagesRecyclerviewAdapter(
                                prescriptionList,
                                requireActivity()
                            )
                        adapter.recyclerViewItemClickWithView2 = object : OnClickOfDoctorAppointment {
                            override fun onItemClick(id: Int) {
                                val imageUrl =
                                    BaseMediaUrls.USERIMAGE.url + adapter.todaysAppointList!![id].e_prescription!!
                                startActivity(TransaprentPopUpActivityForImageShow.newIntent(activity!!, imageUrl))
                            }

                            override fun onAcceptBtnClick(id: String, text: String) {

                            }

                            override fun onRejectBtnBtnClick(id: String, text: String) {

                            }

                        }
                        prescriptionRecyclerView.adapter = adapter
                    } else {
                        llPrescription.visibility = View.GONE
                    }

                    if (response.result.appointmentStatus != null && !TextUtils.isEmpty(response.result.appointmentStatus.trim()) &&
                        response.result.acceptanceStatus != null && !TextUtils.isEmpty(response.result.acceptanceStatus.trim())
                    ) {
                        if (appointmentId != null) {
                            setUpAcceptRejectSection(
                                response.result.appointmentStatus.toLowerCase(Locale.ROOT),
                                response.result.acceptanceStatus.toLowerCase(Locale.ROOT), appointmentId?.toInt()!!
                            )
                        }
                    }

                    when {
                        response.result.acceptanceStatus.equals("Pending") -> {
//                            btnCancel.visibility = View.GONE
////                                btn_rootscare_doctor_consulting.visibility = View.GONE
////                                tv_status.setBackgroundColor(Color.parseColor("#FCDD00"))
//                            tvStatus.background = activity?.let {
//                                ContextCompat.getDrawable(
//                                    it,
//                                    R.drawable.accptance_pending_bg
//                                )
//                            }
                            btnRootscareDoctorConsulting.visibility = View.INVISIBLE
//                            btnSubmitReview.visibility = View.GONE
                        }
                        response.result.acceptanceStatus.equals("Rejected") -> {
//                            btnCancel.visibility = View.GONE
////                                btn_rootscare_doctor_consulting.visibility = View.INVISIBLE
//                            tvStatus.setBackgroundColor(Color.parseColor("#FF0303"))
//                            tvStatus.background = activity?.let {
//                                ContextCompat.getDrawable(
//                                    it,
//                                    R.drawable.reject_bg
//                                )
//                            }
                            btnRootscareDoctorConsulting.visibility = View.INVISIBLE
//                            btnSubmitReview.visibility = View.GONE
                        }
                        response.result.acceptanceStatus.equals("Completed") -> {
//                            btnCancel.visibility = View.GONE
////                                btn_rootscare_doctor_consulting.visibility = View.INVISIBLE
//                            tvStatus.setBackgroundColor(Color.parseColor("#FF0303"))
//                            tvStatus.background = activity?.let {
//                                ContextCompat.getDrawable(
//                                    it,
//                                    R.drawable.reject_bg
//                                )
//                            }
                            btnRootscareDoctorConsulting.visibility = View.INVISIBLE
//                            btnSubmitReview.visibility = View.VISIBLE
                        }
                        else -> {
//                            btnCancel.visibility = View.GONE
//                                btn_rootscare_doctor_consulting.visibility = View.VISIBLE
//                            tvStatus.setBackgroundColor(Color.parseColor("#70BE58"))
//                            tvStatus.background = activity?.let {
//                                ContextCompat.getDrawable(
//                                    it,
//                                    R.drawable.approved_bg
//                                )
//                            }
                            if (statusOfAppointmentForFlowChange == "today" && serviceType == "doctor")
                                btnRootscareDoctorConsulting.visibility = View.VISIBLE
                            else
                                btnRootscareDoctorConsulting.visibility = View.GONE
                        }
                    }

//                    if (response.result.uploadPrescription != null && !TextUtils.isEmpty(response.result.uploadPrescription.trim())) {
//                        ivPrescription.visibility = View.VISIBLE
//                        val options: RequestOptions =
//                            RequestOptions()
//                                .centerCrop()
//                                .placeholder(R.drawable.profile_no_image)
//                                .priority(Priority.HIGH)
//                        Glide
//                            .with(activity!!)
//                            .load(getString(R.string.api_base) + "uploads/images/" + response.result.uploadPrescription)
//                            .apply(options)
//                            .into(ivPrescription)
//                    } else {
//                        ivPrescription.visibility = View.GONE
//                    }

                    ivPrescription.setOnClickListener {
                        val imageUrl = BaseMediaUrls.USERIMAGE.url + response.result.uploadPrescription
                        startActivity(TransaprentPopUpActivityForImageShow.newIntent(requireActivity(), imageUrl))
                    }

                    btnRootscareDoctorConsulting.setOnClickListener {
                        if (isNetworkConnected) {
                            baseActivity?.showLoading()
                            val videoPushRequest = VideoPushRequest()
                            videoPushRequest.fromUserId = doctorId
                            videoPushRequest.toUserId = patientIdForPrescriptionUpload
                            roomName = "rootvideo_room_" + videoPushRequest.toUserId + "_" + videoPushRequest.fromUserId
                            videoPushRequest.orderId = response.result.orderId
                            videoPushRequest.roomName = "rootvideo_room_" + videoPushRequest.toUserId + "_" + videoPushRequest.fromUserId
                            videoPushRequest.type = "doctor_to_patient_video_call"
                            videoPushRequest.fromUserName = response.result.doctorName
                            videoPushRequest.toUserName = response.result.patientName
                            fragmentDoctorAppointmentDetailsViewModel?.apiSendVideoPushNotification(
                                videoPushRequest
                            )

                        } else {
                            Toast.makeText(
                                activity,
                                "Please check your network connection.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun successVideoPushResponse(videoPushResponse: VideoPushResponse) {
        baseActivity?.hideLoading()
        if (videoPushResponse.status!!) {
            val bundle = Bundle()
            bundle.putString("roomName", roomName)
            bundle.putString("fromUserId", doctorId)
            bundle.putString("fromUserName", appointmentDetailsResponse?.result?.doctorName)
            bundle.putString("toUserId", patientIdForPrescriptionUpload)
            bundle.putString("toUserName", appointmentDetailsResponse?.result?.patientName)
            bundle.putString("orderId", appointmentDetailsResponse?.result?.orderId)
            bundle.putBoolean("isDoctorCalling", true)
            val intent = Intent(activity, VideoCallActivity::class.java)
            intent.putExtras(bundle)
            activity?.startActivity(intent)
//            activity?.finish()
        } else {
            Toast.makeText(activity, videoPushResponse.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun errorVideoPushResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
        if (throwable?.message != null) {
            Log.d(TAG, "--ERROR-Throwable:-- ${throwable.message}")
            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onThrowable(throwable: Throwable) {
        baseActivity?.hideLoading()
    }


    private var isPlaying = false
    private var seekposition: Int = 0
    private var totalDuration: Int = 0
    private fun setUpAudioLayout(url: String) {
        with(fragmentDoctorAppointmentDetailsBinding!!) {
//            txtElaspsedTime.base = seekposition.toLong()
            setAudioFileToMediaPlayer(url)

            imgPlay.setOnClickListener {
                if (!isPlaying) {
                    startPlaying()
                    val resourceId: Int? = activity?.resources?.getIdentifier(
                        "pause",
                        "drawable", activity?.packageName
                    )
                    if (resourceId != null) {
                        imgPlay.setImageResource(resourceId)
                    }
                    isPlaying = true
                } else {
                    stopPlaying()
                    val resourceId: Int? = activity?.resources?.getIdentifier(
                        "play",
                        "drawable", activity?.packageName
                    )
                    if (resourceId != null) {
                        imgPlay.setImageResource(resourceId)
                    }
                    isPlaying = false
                }
            }

            //seekbar change listener
            seekBar.max = totalDuration
            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {}
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    seekposition = seekBar.progress
                    mediaPlayer?.seekTo(seekposition)
                    fragmentDoctorAppointmentDetailsBinding?.txtElaspsedTime?.base = SystemClock.elapsedRealtime() - seekposition
                }
            })

            mediaPlayer?.setOnCompletionListener {
                resetAudioFileToMediaPlayer()
            }

        }
    }

    private fun setAudioFileToMediaPlayer(url: String) {
        with(fragmentDoctorAppointmentDetailsBinding!!) {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(url)
            mediaPlayer?.prepare()
            totalDuration = mediaPlayer?.duration!!
            val minutes: Int = totalDuration / 1000 / 60
            val seconds: Int = totalDuration / 1000 % 60
            var tempMinutes = "00"
            var tempSeconds = "00"
            if (minutes != 0) {
                tempMinutes = if (minutes >= 10) {
                    minutes.toString()
                } else {
                    "0$minutes"
                }
            }
            if (seconds != 0) {
                tempSeconds = if (seconds >= 10) {
                    seconds.toString()
                } else {
                    "0$seconds"
                }
            }
            txtTime.text = "$tempMinutes : $tempSeconds"
        }
    }

    var handler: Handler? = null
    var runnable: Runnable? = null
    private fun startPlaying() {
        seekposition = mediaPlayer?.currentPosition!!
        mediaPlayer?.start()

        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                try {
                    if (mediaPlayer?.currentPosition != null) {
                        seekposition = mediaPlayer?.currentPosition!!
                        fragmentDoctorAppointmentDetailsBinding!!.seekBar.progress = seekposition
                        handler?.postDelayed(this, 1000)
                    }
                } catch (ed: IllegalStateException) {
                    ed.printStackTrace()
                }
            }
        }
        handler?.postDelayed(runnable!!, 0)
//        fragmentDoctorAppointmentDetailsBinding?.txtElapsedTime?.start()
        fragmentDoctorAppointmentDetailsBinding?.txtElaspsedTime?.base = SystemClock.elapsedRealtime() - seekposition
        fragmentDoctorAppointmentDetailsBinding?.txtElaspsedTime?.start()
    }

    private fun stopPlaying() {
        if (mediaPlayer !== null) {
            seekposition = mediaPlayer?.currentPosition!!
            mediaPlayer?.pause()
            handler?.removeCallbacks(runnable!!)
            fragmentDoctorAppointmentDetailsBinding?.txtElaspsedTime?.stop()
        }
    }

    private fun resetAudioFileToMediaPlayer() {
        with(fragmentDoctorAppointmentDetailsBinding!!) {
            val resourceId: Int? = activity?.resources?.getIdentifier(
                "play",
                "drawable", activity?.packageName
            )
            if (resourceId != null) {
                imgPlay.setImageResource(resourceId)
            }
            isPlaying = false
            /*stopPlaying()
            seekposition = 0
            seekBar.progress = seekposition
            txtElaspsedTime.base = SystemClock.elapsedRealtime()*/
            txtElaspsedTime.stop()
            val handler = Handler()
            handler.postDelayed({
                txtElaspsedTime.base = SystemClock.elapsedRealtime()
                mediaPlayer?.seekTo(0)
            }, 100)
//            txtElaspsedTime.base = SystemClock.elapsedRealtime() - seekposition
//            txtElaspsedTime.base = seekposition.toLong()
        }
    }

    override fun onStop() {
        stopPlaying()
        super.onStop()
    }

    override fun onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
            if (runnable != null && handler != null) {
                handler?.removeCallbacks(runnable!!)
            }
        }
        super.onDestroy()
    }


    private fun setUpAcceptRejectSection(appointmentStatus: String, acceptanceStatus: String, id: Int) {
        with(fragmentDoctorAppointmentDetailsBinding!!) {
            if (serviceType?.toLowerCase(Locale.ROOT).equals("doctor")) {
                if (statusOfAppointmentForFlowChange != null && statusOfAppointmentForFlowChange?.equals("pending")!!) {
                    llBottomUploadPrescription.visibility = View.GONE
                    if (acceptanceStatus.contains("pending")) {
                        llBottomAcceptReject.visibility = View.VISIBLE
                        btnAccept.setOnClickListener {
                            acceptRejectAppointment(id, "Accept")
                        }
                        btnRejectt.setOnClickListener {
                            acceptRejectAppointment(id, "Reject")
                        }
                    } else {
                        llBottomAcceptReject.visibility = View.GONE
                    }

                    /*if (appointmentStatus.contains("accepted")) {
                        llBottomAcceptReject.visibility = View.GONE
                    }else if (appointmentStatus.contains("complete")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (appointmentStatus.contains("reject")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (appointmentStatus.contains("cancel")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (appointmentStatus.contains("book")) {
                        llBottomAcceptReject.visibility = View.VISIBLE
                        btnAccept.setOnClickListener {
                            acceptRejectAppointment(id, "Accept")
                        }
                        btnRejectt.setOnClickListener {
                            acceptRejectAppointment(id, "Reject")
                        }
                    }*/
                } else if (statusOfAppointmentForFlowChange != null && statusOfAppointmentForFlowChange?.equals("today")!!) {
                    llBottomUploadPrescription.visibility = View.VISIBLE
                    llBottomAcceptReject.visibility = View.GONE
                    if (appointmentStatus.contains("complete")) {
                        btnCompleted.visibility = View.GONE
                        btnUploadPrescription.visibility = View.VISIBLE
                        btnUploadPrescription.setOnClickListener {
                            openCamera()
                        }
                    } else {
                        btnCompleted.visibility = View.VISIBLE
                        btnUploadPrescription.visibility = View.GONE
                        btnCompleted.setOnClickListener {
                            apiHitForCompleted(id.toString())
                        }
                    }
                } else {
                    llBottomAcceptReject.visibility = View.GONE
                    llBottomUploadPrescription.visibility = View.GONE
                }
            } else if (serviceType?.toLowerCase(Locale.ROOT).equals("nurse")) {
                if (statusOfAppointmentForFlowChange != null && statusOfAppointmentForFlowChange?.equals("pending")!!) {
                    llBottomUploadPrescription.visibility = View.GONE
                    if (acceptanceStatus.contains("pending")) {
                        llBottomAcceptReject.visibility = View.VISIBLE
                        btnAccept.setOnClickListener {
                            acceptRejectAppointment(id, "Accept")
                        }
                        btnRejectt.setOnClickListener {
                            acceptRejectAppointment(id, "Reject")
                        }
                    } else {
                        llBottomAcceptReject.visibility = View.GONE
                    }


                    /*if (appointmentStatus.contains("complete")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (appointmentStatus.contains("reject")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (appointmentStatus.contains("cancel")) {
                        llBottomAcceptReject.visibility = View.GONE
                    } else if (appointmentStatus.contains("book")) {
                        llBottomAcceptReject.visibility = View.VISIBLE
                        btnAccept.setOnClickListener {
                            acceptRejectAppointment(id, "Accept")
                        }
                        btnRejectt.setOnClickListener {
                            acceptRejectAppointment(id, "Reject")
                        }
                    }*/
                } else {
                    llBottomAcceptReject.visibility = View.GONE
                    llBottomUploadPrescription.visibility = View.GONE
                }
            }
        }
    }


    private fun openCamera() {
//        mCameraIntentHelper!!.startCameraIntent()
      /*  CropImage.activity()
            .setCropShape(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) CropImageView.CropShape.RECTANGLE
                else (CropImageView.CropShape.OVAL)
            )
            .setInitialCropWindowPaddingRatio(0F)
            .setAspectRatio(1, 1)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setActivityTitle("Crop")
            .setOutputCompressQuality(10)
            .start(requireActivity())*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
      /*  if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            imageSelectionModel = AddlabTestImageSelectionModel()
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                imageSelectionModel?.imageDataFromCropLibrary = result
                val resultUri = result.uri
                if (resultUri != null) { // Get file from cache directory
                    Log.d(TAG, "--check_urii--  $resultUri")
                    FileNameInputDialog(requireActivity(), object : FileNameInputDialog.CallbackAfterDateTimeSelect {
                        override fun selectDateTime(dateTime: String) {
                            val fileCacheDir = File(activity?.cacheDir, resultUri.lastPathSegment)
                            if (fileCacheDir.exists()) {
//                                    imageSelectionModel?.file = fileCacheDir
                                imageSelectionModel?.file = MyImageCompress.compressImageFilGottenFromCache(activity, resultUri, 10)
                                imageSelectionModel?.filePath = resultUri.toString()
                                imageSelectionModel?.rawFileName = resultUri.lastPathSegment
                                var tempNameExtension = ""
                                if (resultUri.lastPathSegment?.contains(".jpg")!!) {
                                    tempNameExtension = ".jpg"
                                } else if (resultUri.lastPathSegment?.contains(".png")!!) {
                                    tempNameExtension = ".png"
                                }
                                imageSelectionModel?.fileName =
                                    "${dateTime}_${DateTimeUtils.getFormattedDate(Date(), "dd/MM/yyyy_HH:mm:ss")}${tempNameExtension}"
//                                    imageSelectionModel.fileNameAsOriginal = "${dateTime}${tempNameExtension}"
                                imageSelectionModel?.fileNameAsOriginal = dateTime
                                if (activity?.contentResolver?.getType(resultUri) == null) {
                                    imageSelectionModel?.type = "image"
                                } else {
                                    imageSelectionModel?.type = activity?.contentResolver?.getType(resultUri)
                                }
                                if (imageSelectionModel?.file != null && imageSelectionModel?.file?.exists()!!) {
                                    uploadPrescription(imageSelectionModel!!)
                                }
                                Log.d("check_path", ": $resultUri")
                                Log.d("check_file_get", ": $fileCacheDir")
                            } else {
                                Log.d("file_does_not_exists", ": " + true)
                            }
                            hideKeyboard()
                        }
                    }).show(requireActivity().supportFragmentManager)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d("check_error", ": " + error.message)
            }
        }*/
    }

    private fun uploadPrescription(imageSelectionModel: AddlabTestImageSelectionModel) {
        if (fragmentDoctorAppointmentDetailsViewModel?.appSharedPref?.loginUserId != null
            && patientIdForPrescriptionUpload != null
        ) {
            baseActivity?.showLoading()
            baseActivity?.hideKeyboard()
            val doctorId = fragmentDoctorAppointmentDetailsViewModel?.appSharedPref?.loginUserId!!
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val patientId = patientIdForPrescriptionUpload!!
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val appointmentId1 = appointmentId!!
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val prescriptionNumber = imageSelectionModel.fileNameAsOriginal
                .toRequestBody("multipart/form-data".toMediaTypeOrNull())
//            fileNameForTempUse = imageSelectionModel.fileNameAsOriginal

            val image = imageSelectionModel.file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val imageMultipartBody: MultipartBody.Part? =
                MultipartBody.Part.createFormData("e_prescription", imageSelectionModel.file?.name, image)
            fragmentDoctorAppointmentDetailsViewModel?.uploadPrescription(
                patientId,
                doctorId,
                appointmentId1,
                prescriptionNumber,
                imageMultipartBody
            )
        }
    }

    private fun apiHitForCompleted(id: String) {
        val request = CommonUserIdRequest()
        request.id = id
        if (isNetworkConnected) {
            baseActivity?.showLoading()
            fragmentDoctorAppointmentDetailsViewModel!!.apiHitForMarkAsComplete(
                request
            )
        } else {
            Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun acceptRejectAppointment(id: Int, status: String) {
        CommonDialog.showDialog(requireActivity(), object :
            DialogClickCallback {
            override fun onDismiss() {

            }

            override fun onConfirm() {
                if (isNetworkConnected) {
                    baseActivity?.showLoading()
                    val updateAppointmentRequest = UpdateAppointmentRequest()
                    updateAppointmentRequest.id = id.toString()
                    updateAppointmentRequest.acceptanceStatus = status
                    fragmentDoctorAppointmentDetailsViewModel!!.apiupdatedoctorappointmentrequest(updateAppointmentRequest)
                } else {
                    Toast.makeText(activity, "Please check your network connection.", Toast.LENGTH_SHORT).show()
                }
            }

        }, "Confirmation", "Are you sure to ${status.toLowerCase(Locale.ROOT)} this appointment?")
    }

    override fun successGetDoctorRequestAppointmentUpdateResponse(getDoctorRequestAppointmentResponse: GetDoctorRequestAppointmentResponse?) {
        baseActivity?.hideLoading()
        apiHitForDetails()
    }

    override fun errorGetDoctorRequestAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
    }

    override fun onSuccessMarkAsComplete(response: CommonResponse) {
        baseActivity?.hideLoading()
        apiHitForDetails()
    }

    override fun errorGetDoctorTodaysAppointmentResponse(throwable: Throwable?) {
        baseActivity?.hideLoading()
    }

    override fun onSuccessUploadPrescription(response: CommonResponse) {
        baseActivity?.hideLoading()
        apiHitForDetails()
    }

    private fun formatDateFromString(inputFormat: String?, outputFormat: String?, inputDate: String?): String {
        val parsed: Date?
        var outputDate = ""
        val df_input =
            SimpleDateFormat(inputFormat, Locale.getDefault())
        val df_output =
            SimpleDateFormat(outputFormat, Locale.getDefault())
        try {
            parsed = df_input.parse(inputDate)
            outputDate = df_output.format(parsed)
        } catch (e: ParseException) {
        }
        return outputDate
    }


}