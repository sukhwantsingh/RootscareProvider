package com.rootscare.twilio

import android.Manifest
import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.SystemClock
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ApplicationClass
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import com.rootscare.data.datasource.sharedpref.AppSharedPref
import com.rootscare.data.model.request.twilio.TwilioAccessTokenRequest
import com.rootscare.data.model.request.videoPushRequest.VideoPushRequest
import com.rootscare.data.model.response.loginresponse.LoginResponse
import com.rootscare.serviceprovider.R
import com.rootscare.utils.CommonUtils
import com.twilio.video.*
import com.twilio.video.VideoView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import tvi.webrtc.VideoSink
import java.util.*
import kotlin.math.ln

class VideoCallActivity : AppCompatActivity() {

    private var accessToken: String? = null
    private var localParticipant: LocalParticipant? = null


    private var primaryVideoView: VideoView? = null
    private var thumbnailVideoView: VideoView? = null
    private var clVideo: CoordinatorLayout? = null

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()


    //   private TextView videoStatusTextView;
    private var cameraCaptureCompat: CameraCaptureCompat? = null
    private var localAudioTrack: LocalAudioTrack? = null
    private var localVideoTrack: LocalVideoTrack? = null
    private var connectActionFab: FloatingActionButton? = null
    private var switchCameraActionFab: FloatingActionButton? = null
    private var muteActionFab: FloatingActionButton? = null
    private var audioManager: AudioManager? = null
    private var remoteParticipantIdentity: String? = null

    private var chronometer: Chronometer? = null
    private var vibrator: Vibrator? = null
    private var previousAudioMode = 0
    private var previousMicrophoneMute = false
    private var localVideoView: VideoSink? = null
    private var disconnectedFromOnDestroy = false

    private var rl01: RelativeLayout? = null

    private var timer: Timer? = null
    private var timer1: Timer? = null

    private var timeCounter = 0
    private var timerCall = 0
    private var timeFees = 0
    private var timeWhenStopped: Long = 0
    private var mediaPlayer: MediaPlayer? = null

    //    private var userId: String? = null
    private var clientOwnName: String? = null
    private var roomName: String? = ""
    private var fromUserName: String? = null
    private var toUserName: String? = null
    private var fromUserId: String? = null
    private var toUserId: String? = null
    private var clientPersonName: String? = null
    private var orderId: String? = null
    private var isDoctorCalling: Boolean? = null

//    private var loginResponse: LoginResponse? = null

    val appSharedPref: AppSharedPref?
        get() = ApplicationClass.instance!!.appSharedPref


    companion object {
        private const val CAMERA_MIC_PERMISSION_REQUEST_CODE = 1
        private const val TAG = "VideoCallActivity"

        var room: Room? = null

        var isCallTaken = false
        var isReject = false
        var appCompatActivity: AppCompatActivity? = null
        var alertDialog: AlertDialog? = null

        fun newIntent(activity: Activity): Intent {
            return Intent(activity, VideoCallActivity::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
        setContentView(R.layout.activity_video)
        try {
            SoundPoolManager.getInstance(this)?.stopRinging()
        } catch (e:Exception){
            Log.e("video_call",e.toString())
        }
        appCompatActivity = this
        readFromBundle()
        statusOfCall
    }

    // => Date is in UTC now
    private val statusOfCall: Unit
        get() {
            initView()
            timeCounter = 0
            timerCall = 0
            timeFees = 0
            calculateTimer()
            Video.setLogLevel(LogLevel.OFF)
            /*
             * Enable changing the volume using the up/down keys during a conversation
             */

            volumeControlStream = AudioManager.STREAM_VOICE_CALL

            /*
             * Needed for setting/abandoning audio focus during call
             */
            audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
            audioManager!!.isSpeakerphoneOn = true

            val gson = Gson()
            val loginModelDataString: String = appSharedPref?.loginmodeldata!!
            val loginResponse: LoginResponse = gson.fromJson(loginModelDataString, LoginResponse::class.java)

            clientOwnName = (loginResponse.result?.firstName + loginResponse.result?.lastName).replace("\\s", "")

            /*
             * Check camera and microphone permissions. Needed in Android M.
             */
            if (checkPermissionForCameraAndMicrophone()) {
                requestPermissionForCameraAndMicrophone()
            } else {
                if (isDoctorCalling!!) {
                    createAudioAndVideoTracks()
                    setAccessToken()
                    /*
                    * Set the initial state of the UI
                    */
                    initializeUI()
                } else if (!roomName.equals("", ignoreCase = true)) {
                    val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                    val am = this.getSystemService(AUDIO_SERVICE) as AudioManager
                    val volumeLevel1 = am.getStreamVolume(AudioManager.STREAM_RING)
                    val maxVolume = am.getStreamMaxVolume(AudioManager.STREAM_RING)
                    mediaPlayer = MediaPlayer.create(this, notification)
                    mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    val log1 = (1 - ln(maxVolume - volumeLevel1.toDouble()) / ln(maxVolume.toDouble())).toFloat()
                    mediaPlayer?.setVolume(log1, log1)
                    vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
                    val pattern = longArrayOf(0, 500, 1000)
                    vibrator!!.vibrate(pattern, 0)
                    mediaPlayer?.start()
                    showVideoIncomingDialog(fromUserName)
                }
            }

        }

    private fun initView() {
        val tvHeader = findViewById<TextView>(R.id.tv_header)
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        val tvOrderId = findViewById<TextView>(R.id.tv_order_id)
        primaryVideoView = findViewById(R.id.primary_video_view)
        thumbnailVideoView = findViewById(R.id.thumbnail_video_view)
//        videoStatusTextView = findViewById(R.id.video_status_textview);
        connectActionFab = findViewById(R.id.connect_action_fab)
        chronometer = findViewById(R.id.chronometer)
//        connectActionFab?.isEnabled = true
        switchCameraActionFab = findViewById(R.id.switch_camera_action_fab)
//        val btnLastCall = findViewById<Button>(R.id.btn_lastCall)
        muteActionFab = findViewById(R.id.mute_action_fab)
        clVideo = findViewById(R.id.cl_video)
        rl01 = findViewById(R.id.rl_01)

        tvHeader.visibility = View.VISIBLE
        tvHeader.text = resources.getString(R.string.heading_video_call)
        btnBack.visibility = View.GONE
        tvOrderId?.text = orderId
//        chronometer!!.visibility = View.GONE
//        btnLastCall.visibility = View.GONE
    }

    private fun readFromBundle() {
        fromUserName = intent.extras!!.getString("fromUserName")
        fromUserId = intent.extras!!.getString("fromUserId")
        toUserName = intent.extras!!.getString("toUserName")
        toUserId = intent.extras!!.getString("toUserId")
        roomName = intent.extras!!.getString("roomName")
        orderId = intent.extras!!.getString("orderId")
        isDoctorCalling = intent.extras!!.getBoolean("isDoctorCalling")
        Log.e("video_call","roomName $roomName")
        clientPersonName = fromUserName
//        Log.e("video_call","isTeacherCalling $isTeacherCalling")
    }

    private fun showVideoIncomingDialog(name: String?) {
        alertDialog = name?.let {
            Dialog.createStudentIncomingCallDialog(
                answerVideoCallClickListener(), cancelVideoCallClickListener(),this, it)
        }
        alertDialog!!.setCanceledOnTouchOutside(false)
        alertDialog!!.setCancelable(false)
        alertDialog!!.show()
    }

    private fun requestPermissions(): Boolean {
        val isCheck = BooleanArray(1)
        val permissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )
        val rationale = "Please provide camera, storage & recording permission so that you can ..."
        val options: Permissions.Options =
            Permissions.Options().setRationaleDialogTitle("Info").setSettingsDialogTitle("Warning")
        Permissions.check(this, permissions, rationale, options, object : PermissionHandler() {
            override fun onGranted() {
                isCheck[0] = true
//                Toast.makeText(VideoCallActivity.this, "Camera, Storage & Recording permissions granted.", Toast.LENGTH_SHORT).show();
            }

            override fun onDenied(context: Context?, deniedPermissions: ArrayList<String?>?) {
                isCheck[0] = false
                Toast.makeText(this@VideoCallActivity, "Permission denied.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        return isCheck[0]
    }

    private fun calculateTimer() {
        timer1 = Timer()
        timer1!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    timerCall++
                    if (timerCall % 30 == 0 && !isCallTaken) {
                        if (isDoctorCalling!!) {
//                            callServiceForStatusChange();
                            if (room != null) {
                                room!!.disconnect()
                            }
                            chronometer!!.stop()
                            timer1?.cancel()
                            initializeUI()
                            if (vibrator != null) {
                                vibrator!!.cancel()
                                vibrator = null
                            }
                            if (mediaPlayer != null) {
                                mediaPlayer!!.stop()
                                mediaPlayer = null
                            }
                            if (alertDialog != null) {
                                if (alertDialog!!.isShowing) alertDialog!!.dismiss()
                                alertDialog = null
                            }

                            initializeUI()
                            finishAndRemoveTask()

                        } else {
                            if (room != null) {
                                room!!.disconnect()
                            }
                            chronometer!!.stop()
                            timer1?.cancel()
                            initializeUI()
                            if (vibrator != null) {
                                vibrator!!.cancel()
                                vibrator = null
                            }
                            if (mediaPlayer != null) {
                                mediaPlayer!!.stop()
                                mediaPlayer = null
                            }
                            if (alertDialog != null) {
                                if (alertDialog!!.isShowing) alertDialog!!.dismiss()
                                alertDialog = null
                            }

                            initializeUI()
                            finishAndRemoveTask()

                        }
                    }
                }
            }
        }, 1000, 1000)
    }

    private fun calculateFee() {
        timer = Timer()
        timer!!.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    timeCounter++
//                    Log.e("video_call","minute  $minute")
//                    Log.e("video_call","minute_mod $minuteMod")
                    Log.e("video_call","time_counter $timeCounter")
                }
            }
        }, 1000, 1000)
    }


    private val mInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (intent.action == Intent.ACTION_HEADSET_PLUG) {
                Log.e("video_call","musicIntentReceiver BroadcastReceiver ----------")

                when (intent.getIntExtra("state", -1)) {
                    1 -> if (audioManager != null) audioManager!!.isSpeakerphoneOn = false
                    0 -> if (audioManager != null) audioManager!!.isSpeakerphoneOn = true
                    else -> if (audioManager != null) audioManager!!.isSpeakerphoneOn = true
                }
            } else if(intent.action == Intent.ACTION_BATTERY_CHANGED){
                val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
                if (!CommonUtils.isCharging(this@VideoCallActivity)) {
                    if (level <= 10) {
                        Snackbar.make(
                            clVideo!!,
                            "Your battery level is low. Please plug into charger to continue",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    if (level <= 5) {
                        if (room != null) {
                            if (room?.remoteParticipants != null) {
                                isCallTaken = false
                                if (room?.remoteParticipants?.size == 1)
                                    if (timeCounter > 20) {
                                        room?.disconnect()
                                        chronometer!!.stop()
                                        timer?.cancel()
                                        timer1?.cancel()
                                        initializeUI()
                                        finishAndRemoveTask()

                                    }
                            } else {
                                isCallTaken = false
                                room?.disconnect()
                                chronometer?.stop()
                                if (timer != null) timer?.cancel()
                                if (timer1 != null) timer1?.cancel()

                                initializeUI()
                                finishAndRemoveTask()
                            }
                        }
                    }
                }
            }


        }
    }

    private fun answerVideoCallClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
            /*
             * Accept an incoming call
             */initializeUI()
            createAudioAndVideoTracks()
            setAccessToken()
            vibrator!!.cancel()
            vibrator = null
            mediaPlayer!!.stop()
            mediaPlayer = null
            alertDialog!!.dismiss()
            isCallTaken = true
        }
    }

    private fun cancelVideoCallClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
            isCallTaken = true
            callRejectPushNotification("Doctor $toUserName rejects the call.")
        }
    }

    private fun waitVideoCallClickListener(): DialogInterface.OnClickListener {
        return DialogInterface.OnClickListener { _: DialogInterface?, _: Int ->
            isCallTaken = true
        }
    }

    private fun disconnectClickListener(): View.OnClickListener {
        return View.OnClickListener {
//            connectActionFab!!.isEnabled = false
            Log.e("video_call","disconnectClickListener $room")
            isCallTaken = false
            /*
             * Disconnect from room
             */
//            Log.e("video_call","localParticipant.getVideoTracks().size() " + room.getParticipants().size());

//            System.out.Log.e("video_call","localParticipant.getVideoTracks().size() " + room.getParticipants().size());
            Log.e("video_call","room.getRemoteParticipants().size() " + room!!.remoteParticipants.size)
            if (room != null) {
                if (room?.remoteParticipants?.size == 1) {
                    Log.e("video_call","test  if$isCallTaken")
                    if (timeCounter > 60) {
                        Log.e("video_call","test time_counter if")
                        disconnectCall()
                    } else {
                        room?.disconnect()
                        chronometer?.stop()
                        timer?.cancel()
                        timer1?.cancel()

                        finishAndRemoveTask()
                    }
                } else {
                   callRejectPushNotification("Doctor $toUserName rejects the call.")
                   Log.e("video_call","callRejectPushNotification")
//                  if (!isCallTaken && !isTeacherCalling)
//                    room?.disconnect()
//                    chronometer?.stop()
//                    timer?.cancel()
//                    timer1?.cancel()
//
//                    finishAndRemoveTask()
                }
            } else finishAndRemoveTask()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_MIC_PERMISSION_REQUEST_CODE) {
            var cameraAndMicPermissionGranted = true
            for (grantResult in grantResults) {
                cameraAndMicPermissionGranted =
                    cameraAndMicPermissionGranted and (grantResult == PackageManager.PERMISSION_GRANTED)
            }
            if (cameraAndMicPermissionGranted) {
                createAudioAndVideoTracks()
                setAccessToken()
            } else {
                Toast.makeText(this, R.string.permissions_needed, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        this.registerReceiver(mInfoReceiver, IntentFilter(Intent.ACTION_HEADSET_PLUG).apply { addAction(Intent.ACTION_BATTERY_CHANGED) })

        /*
         * If the local video track was released when the app was put in the background, recreate.
         */if (localVideoTrack == null && requestPermissions() && cameraCaptureCompat != null) {
            localVideoTrack =
                cameraCaptureCompat?.let { LocalVideoTrack.create(this, true, it) }
            localVideoTrack?.addSink(localVideoView!!)

            /*
             * If connected to a Room then share the local video track.
             */Log.e("video_call","localParticipant $localParticipant")
            if (localParticipant != null) {
                localVideoTrack?.let { localParticipant!!.publishTrack(it) }
            }
        }
//        chronometer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
//        chronometer.start();
    }

    override fun onPause() {
        Log.e("video_call","onPause Video Call")
        /*
         * Release the local video track before going in the background. This ensures that the
         * camera can be used by other applications while this app is in the background.
         */
        rl01!!.visibility = View.GONE
        if (localVideoTrack != null) {
            /*
             * If this local video track is being shared in a Room, remove from local
             * participant before releasing the video track. Participants will be notified that
             * the track has been removed.
             */
            if (localParticipant != null) {
                localParticipant!!.unpublishTrack(localVideoTrack!!)
            }
            localVideoTrack!!.release()
            localVideoTrack = null
        }
        super.onPause()
        unregisterReceiver(mInfoReceiver)
    }


    override fun onDestroy() {
        Log.e("video_call","hello onDestroy")
        /*
         * Always disconnect from the room before leaving the Activity to ensure any memory allocated to the Room resource is freed.
         */
        if (room != null && room!!.state != Room.State.DISCONNECTED) {
            room!!.disconnect()
            disconnectedFromOnDestroy = true
        }

        /*
         * Release the local audio and video tracks ensuring any memory allocated to audio or video is freed.
         */
        if (localAudioTrack != null) {
            localAudioTrack!!.release()
            localAudioTrack = null
        }
        if (localVideoTrack != null) {
            localVideoTrack!!.release()
            localVideoTrack = null
        }
//        if (timer != null) timer!!.cancel()
//        if (timer1 != null) timer1!!.cancel()
        if (vibrator != null) {
            vibrator!!.cancel()
            vibrator = null
        }
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer = null
        }
        isReject = false
        super.onDestroy()
        //        LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mBatInfoReceiver);
    }

    private fun checkPermissionForCameraAndMicrophone(): Boolean {
        val resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        return resultCamera != PackageManager.PERMISSION_GRANTED || resultMic != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
            ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.RECORD_AUDIO
            )
        ) {
            Toast.makeText(this, R.string.permissions_needed, Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                CAMERA_MIC_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun createAudioAndVideoTracks() {
        // Share your microphone
        localAudioTrack = LocalAudioTrack.create(this, true)

        // Share your camera
        cameraCaptureCompat = CameraCaptureCompat(this, CameraCaptureCompat.Source.FRONT_CAMERA)
        localVideoTrack =
            cameraCaptureCompat?.let { LocalVideoTrack.create(this, true, it) }
        primaryVideoView!!.mirror = true
        localVideoTrack?.addSink(primaryVideoView!!)
        localVideoView = primaryVideoView
    }

    private fun setAccessToken() {
        // OPTION 1- Generate an access token from the getting started portal
        // https://www.twilio.com/console/video/dev-tools/testing-tools
//        this.accessToken = TWILIO_ACCESS_TOKEN;

        // OPTION 2- Retrieve an access token from your own web app
//        retrieveAccessTokenFromServer()
        val twilioAccessTokenRequest = TwilioAccessTokenRequest()
        twilioAccessTokenRequest.identity = appSharedPref?.loginUserId
        twilioAccessTokenRequest.roomName = roomName
//        Log.e("video_call","twilioAccessTokenRequest $twilioAccessTokenRequest")
        getAccessToken(twilioAccessTokenRequest)
    }

    private fun connectToRoom() {

        configureAudio(true)
        val connectOptionsBuilder = ConnectOptions.Builder(accessToken?:"").roomName(roomName?:"")

        /*
         * Add local audio track to connect options to share with participants.
         */
        if (localAudioTrack != null) {
            connectOptionsBuilder.audioTracks(listOf(localAudioTrack))
        }

        /*
         * Add local video track to connect options to share with participants.
         */if (localVideoTrack != null) {
            connectOptionsBuilder.videoTracks(listOf(localVideoTrack))
        }
        room = Video.connect(this, connectOptionsBuilder.build(), roomListener())
//        Log.e("video_call","room " + room.isRecording)
        setDisconnectAction()
    }

    /*
     * The initial state when there is no active room.
     */
    private fun initializeUI() {
        switchCameraActionFab!!.show()
        switchCameraActionFab!!.setOnClickListener(switchCameraClickListener())
        muteActionFab?.show()
        muteActionFab?.setOnClickListener(muteClickListener())
    }

    /*
     * The actions performed during disconnect.
     */
    private fun setDisconnectAction() {
        connectActionFab!!.setOnClickListener(disconnectClickListener())
    }

    /*
    * Creates an connect UI dialog
    */

    /*
     * Called when remote participant joins the room
     */
    private fun addRemoteParticipant(remoteParticipant: RemoteParticipant) {
        /*
         * This app only displays video for one additional participant per Room
         */
        if (thumbnailVideoView!!.visibility == View.VISIBLE) {
            Snackbar.make(
                clVideo!!,
                "Rendering multiple participants not supported in this app",
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
        remoteParticipantIdentity = remoteParticipant.identity
        //        statusTextView.setText("RemoteParticipant " + remoteParticipantIdentity + " joined");

        /*
         * Add remote participant renderer
         */if (remoteParticipant.remoteVideoTracks.size > 0) {
            val remoteVideoTrackPublication = remoteParticipant.remoteVideoTracks[0]

            /*
             * Only render video tracks that are subscribed to
             */
            if (remoteVideoTrackPublication.isTrackSubscribed) {
                remoteVideoTrackPublication.remoteVideoTrack?.let { addRemoteParticipantVideo(it) }
            }
        }

        /*
         * Start listening for participant media events
         */remoteParticipant.setListener(mediaListener())
    }

    /*
     * Set primary view as renderer for participant video track
     */
    private fun addRemoteParticipantVideo(videoTrack: VideoTrack) {
        moveLocalVideoToThumbnailView()
        primaryVideoView!!.mirror = false
        videoTrack.addSink(primaryVideoView!!)
        chronometer!!.visibility = View.VISIBLE
        chronometer!!.start()
        chronometer!!.base = SystemClock.elapsedRealtime()
    }

    private fun moveLocalVideoToThumbnailView() {
        if (thumbnailVideoView!!.visibility == View.GONE) {
            thumbnailVideoView!!.visibility = View.VISIBLE
            try {
                localVideoTrack!!.removeSink(primaryVideoView!!)
                localVideoTrack!!.addSink(thumbnailVideoView!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            localVideoView = thumbnailVideoView
            thumbnailVideoView!!.mirror = cameraCaptureCompat!!.cameraSource == CameraCaptureCompat.Source.FRONT_CAMERA
        }
    }

    /*
     * Called when participant leaves the room
     */
    private fun removeParticipant(participant: RemoteParticipant) {
//        videoStatusTextView.setText("Participant " + participant.getIdentity() + " left.");
        if (participant.identity != remoteParticipantIdentity) {
            return
        }
        /*
         * Remove participant renderer
         */if (participant.remoteVideoTracks.size > 0) {
            val remoteVideoTrackPublication = participant.remoteVideoTracks[0]
            if (remoteVideoTrackPublication.isTrackSubscribed) {
                remoteVideoTrackPublication.remoteVideoTrack?.let { removeParticipantVideo(it) }
            }
        }
        moveLocalVideoToPrimaryView()
        if (room != null) {
            room!!.disconnect()
        }
        finishAndRemoveTask()
    }

    private fun removeParticipantVideo(videoTrack: VideoTrack) {
        videoTrack.removeSink(primaryVideoView!!)
    }

    private fun moveLocalVideoToPrimaryView() {
        if (thumbnailVideoView!!.visibility == View.VISIBLE) {
            try {
                localVideoTrack!!.removeSink(thumbnailVideoView!!)
                localVideoTrack!!.addSink(primaryVideoView!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            thumbnailVideoView!!.visibility = View.GONE
            localVideoView = primaryVideoView
            primaryVideoView!!.mirror =
                cameraCaptureCompat!!.cameraSource == CameraCaptureCompat.Source.FRONT_CAMERA
        }
    }

    /*
     * Room events listener
     */
    private fun roomListener(): Room.Listener {
        return object : Room.Listener {
            override fun onReconnected(room: Room) {}
            override fun onReconnecting(room: Room, twilioException: TwilioException) {}
            override fun onConnected(room: Room) {
                Log.e("video_call","onConnected")
                Log.e("video_call","room getLocalParticipant " + room.localParticipant)
                Log.e("video_call","room id " + room.sid)
//                if (isTeacherCalling) {
//                    callToUpdateRoomSid(room.sid)
//                }
                localParticipant = room.localParticipant
                //                videoStatusTextView.setText("Connected to " + room.getName());
                title = room.name
                Log.e("video_call","room getParticipants " + room.remoteParticipants.size)
                for (participant in room.remoteParticipants) {
                    addRemoteParticipant(participant)
                    break
                }
            }

            override fun onConnectFailure(room: Room, e: TwilioException) {
                Log.e("video_call","onConnectFailure")
                e.printStackTrace()
                //                videoStatusTextView.setText("Failed to connect");
                configureAudio(false)
            }

            override fun onDisconnected(room: Room, e: TwilioException?) {
                Log.e("video_call","onDisconnected")
                localParticipant = null
                //                videoStatusTextView.setText("Disconnected from " + room.getName());
                VideoCallActivity.room = null
                // Only reinitialize the UI if disconnect was not called from onDestroy()
                if (!disconnectedFromOnDestroy) {
                    configureAudio(false)
                    initializeUI()
                    moveLocalVideoToPrimaryView()
                    alertDialog?.dismiss()
                }
            }

            override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
                Log.e("video_call","onParticipantConnected")
                isCallTaken = true
                addRemoteParticipant(remoteParticipant)
            }

            override fun onParticipantDisconnected(
                room: Room,
                remoteParticipant: RemoteParticipant
            ) {
                Log.e("video_call","onParticipantDisconnected")
                removeParticipant(remoteParticipant)
            }

            override fun onRecordingStarted(room: Room) {
                /*
                 * Indicates when media shared to a Room is being recorded. Note that recording is only available in our Group Rooms developer preview.
                 */
                Log.wtf(TAG, "onRecordingStarted")
            }

            override fun onRecordingStopped(room: Room) {
                /*
                 * Indicates when media shared to a Room is no longer being recorded. Note that recording is only available in our Group Rooms developer
                 * preview.
                 */
                Log.wtf(TAG, "onRecordingStopped")
            }
        }
    }

    private fun mediaListener(): RemoteParticipant.Listener {
        return object : RemoteParticipant.Listener {
            override fun onAudioTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.e("video_call","onAudioTrackPublished")
            }

            override fun onAudioTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.e("video_call","onAudioTrackUnpublished")
            }

            override fun onVideoTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.e("video_call","onVideoTrackPublished")
                rl01!!.visibility = View.GONE
            }

            override fun onVideoTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.e("video_call","onVideoTrackUnpublished")
                rl01!!.visibility = View.VISIBLE
            }

            override fun onDataTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication
            ) {
                Log.e("video_call","onDataTrackPublished")
            }

            override fun onDataTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication
            ) {
                Log.e("video_call","onDataTrackUnpublished")
            }

            override fun onAudioTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                remoteAudioTrack: RemoteAudioTrack
            ) {
                Log.e("video_call","onAudioTrackSubscribed")
                if (timer1 != null) timer1!!.cancel()
                calculateFee()
            }

            override fun onAudioTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                remoteAudioTrack: RemoteAudioTrack
            ) {
                Log.e("video_call","onAudioTrackUnsubscribed")
            }

            override fun onAudioTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                twilioException: TwilioException
            ) {
                Log.e("video_call","onAudioTrackSubscriptionFailed")
            }

            override fun onVideoTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                Log.e("video_call","onVideoTrackSubscribed")
                rl01!!.visibility = View.GONE
                if (thumbnailVideoView != null) {
                    addRemoteParticipantVideo(remoteVideoTrack)
                    chronometer!!.base = SystemClock.elapsedRealtime() + timeWhenStopped
                    chronometer!!.start()
                    if (primaryVideoView!!.visibility != View.VISIBLE) primaryVideoView!!.visibility =
                        View.VISIBLE
                }
            }

            override fun onVideoTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                Log.e("video_call","onVideoTrackUnsubscribed")
                if (thumbnailVideoView != null) {
                    removeParticipantVideo(remoteVideoTrack)
                    rl01!!.visibility = View.GONE
                    thumbnailVideoView!!.visibility = View.VISIBLE
                    primaryVideoView!!.visibility = View.GONE
                    timeWhenStopped = chronometer!!.base - SystemClock.elapsedRealtime()
                    chronometer!!.stop()
                }
            }

            override fun onVideoTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                twilioException: TwilioException
            ) {
                Log.e("video_call","onVideoTrackSubscriptionFailed")
                Snackbar.make(
                    clVideo!!,
                    String.format(
                        "Failed to subscribe to %s video track",
                        remoteParticipant.identity
                    ),
                    Snackbar.LENGTH_LONG
                ).show()
            }

            override fun onDataTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                remoteDataTrack: RemoteDataTrack
            ) {
                Log.e("video_call","onDataTrackSubscribed")
            }

            override fun onDataTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                remoteDataTrack: RemoteDataTrack
            ) {
                Log.e("video_call","onDataTrackUnsubscribed")
            }

            override fun onDataTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                twilioException: TwilioException
            ) {
                Log.e("video_call","onDataTrackSubscriptionFailed")
            }

            override fun onAudioTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.e("video_call","onAudioTrackEnabled")
            }

            override fun onAudioTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                Log.e("video_call","onAudioTrackDisabled")
            }

            override fun onVideoTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.e("video_call","onVideoTrackEnabled")
            }

            override fun onVideoTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.e("video_call","onVideoTrackDisabled")
            }
        }
    }

    private fun switchCameraClickListener(): View.OnClickListener {
        return View.OnClickListener {
            if (cameraCaptureCompat != null) {
                val cameraSource = cameraCaptureCompat!!.cameraSource
                cameraCaptureCompat!!.switchCamera()
                if (thumbnailVideoView!!.visibility == View.VISIBLE) {
                    thumbnailVideoView!!.mirror =
                        cameraSource ==  CameraCaptureCompat.Source.BACK_CAMERA
                } else {
                    primaryVideoView!!.mirror =
                        cameraSource == CameraCaptureCompat.Source.BACK_CAMERA
                }
            }
        }
    }

    private fun muteClickListener(): View.OnClickListener {
        return View.OnClickListener {
            /*
             * Enable/disable the local audio track. The results of this operation are
             * signaled to other Participants in the same Room. When an audio track is
             * disabled, the audio is muted.
             */if (localAudioTrack != null) {
            val enable = !localAudioTrack!!.isEnabled
            localAudioTrack!!.enable(enable)
            val icon: Int =
                if (enable) R.drawable.ic_mic_green_24px else R.drawable.ic_mic_off_red_24px
            muteActionFab!!.setImageDrawable(
                ContextCompat.getDrawable(
                    this@VideoCallActivity,
                    icon
                )
            )
        }
        }
    }

//    private fun retrieveAccessTokenFromServer() {
//        Ion.with(this)
//            .load(AppConstants.TWILIO_CALL_URL)
//            .setHeader("x-api-key", "Shyam@12345")
//            .setHeader("Content-Type", "application/json")
//            .setLogging("ION_VERBOSE_LOGGING", Log.VERBOSE)
//            .setBodyParameter("identity", "18")
//            .setBodyParameter("room_name", "PA66422")
//            .asJsonObject()
//            .setCallback { e: Exception?, result: JsonObject ->
//                if (e == null) {
//                    Log.e("video_call","result $result")
//                    accessToken = result["token"].asString
//                    if (accessToken != null) connectToRoom()
//                    Log.e("video_call","accessToken $accessToken")
//                } else {
//                    e.printStackTrace()
//                    Toast.makeText(
//                        this@VideoCallActivity,
//                        R.string.error_retrieving_access_token,
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//            }
//    }

    private fun getAccessToken(twilioAccessTokenRequest: TwilioAccessTokenRequest) {
        val disposable =
            ApplicationClass.instance!!.apiServiceWithGsonFactory.getAccessTokenForVideo(
                twilioAccessTokenRequest
            )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response != null) {
                        // Store last login time
                        Log.wtf("check_response", ": " + Gson().toJson(response))
//                    navigator.successLoginResponse(response)
                        /* Saving access token after sign up or login */
                        accessToken = response.result
                        if (accessToken != null) connectToRoom()
                        Log.e("video_call","accessToken $accessToken")

                    } else {
                        Log.wtf("check_response", ": null response")
                    }
                }, { throwable ->
                    run {
//                    navigator.errorLoginResponse(throwable)
                        Log.wtf("check_response_error", ": " + throwable.message)
                    }
                })

        compositeDisposable.add(disposable)
    }

    private fun configureAudio(enable: Boolean) {
        if (enable) {
            previousAudioMode = audioManager!!.mode
            // Request audio focus before making any device switch.
            audioManager!!.requestAudioFocus(
                null,
                AudioManager.STREAM_VOICE_CALL,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
            /*
             * Use MODE_IN_COMMUNICATION as the default audio mode. It is required
             * to be in this mode when play out and/or recording starts for the best
             * possible VoIP performance. Some devices have difficulties with
             * speaker mode if this is not set.
             */audioManager!!.mode = AudioManager.MODE_IN_COMMUNICATION
            /*
             * Always disable microphone mute during a WebRTC call.
             */previousMicrophoneMute = audioManager!!.isMicrophoneMute
            audioManager!!.isMicrophoneMute = false
        } else {
            audioManager!!.mode = previousAudioMode
            audioManager!!.abandonAudioFocus(null)
            audioManager!!.isMicrophoneMute = previousMicrophoneMute
        }
    }

    override fun onStop() {
        super.onStop()
        Log.e("video_call","onStop")
   }

    override fun onBackPressed() {
        connectActionFab?.performClick()
    }

    private fun callRejectPushNotification(messageBody: String) {
        val videoPushRequest = VideoPushRequest()
        videoPushRequest.fromUserId = fromUserId
        videoPushRequest.toUserId = toUserId
        videoPushRequest.orderId = orderId
        videoPushRequest.roomName = roomName
        videoPushRequest.type = "doctor_to_patient_video_call_reject"
        videoPushRequest.fromUserName = toUserName
        videoPushRequest.toUserName = fromUserName
        sendPushNotification(videoPushRequest)
    }

    private fun sendPushNotification(videoPushRequest: VideoPushRequest) {
        val disposable =
            ApplicationClass.instance!!.apiServiceWithGsonFactory.sendVideoPushNotification(videoPushRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response != null) {
                        // Store last login time
                        Log.wtf("check_response", ": " + Gson().toJson(response))
                        /* Saving access token after sign up or login */
                        if (response.status == true) {
                            room?.disconnect()
                            initializeUI()
                            createAudioAndVideoTracks()
                            if (mediaPlayer != null) {
                                mediaPlayer?.stop()
                                mediaPlayer = null
                            }
                            if (vibrator != null) {
                                vibrator?.cancel()
                                vibrator = null
                            }
                            finishAndRemoveTask()
                        } else {
                            Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.wtf("check_response", ": null response")
                    }
                }, { throwable ->
                    run {
//                    navigator.errorLoginResponse(throwable)
                        Log.wtf("check_response_error", ": " + throwable.message)
                    }
                })

        compositeDisposable.add(disposable)
    }

    private fun disconnectCall() {
        val disposable =
            ApplicationClass.instance!!.apiServiceWithGsonFactory.disconnectCall("disconnect", orderId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ response ->
                    if (response != null) {
                        val jsonObject: JsonObject = response
                        val status = jsonObject["status"].asBoolean
                        val message = jsonObject["message"].asString
                        if (status) {
                            room?.disconnect()
                            chronometer?.stop()
                            timer?.cancel()
                            timer1?.cancel()
                            initializeUI()
                            finishAndRemoveTask()

                        } else {
                            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.wtf("check_response", ": null response")
                    }
                }, { throwable ->
                    run {
//                    navigator.errorLoginResponse(throwable)
                        Log.wtf("check_response_error", ": " + throwable.message)
                    }
                })

        if (disposable != null) {
            compositeDisposable.add(disposable)
        }
    }

}