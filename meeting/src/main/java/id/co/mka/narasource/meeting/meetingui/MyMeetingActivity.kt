package id.co.mka.narasource.meeting.meetingui

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.core.utils.DateUtils
import id.co.mka.narasource.core.utils.TimerService
import id.co.mka.narasource.core.utils.getTimeStringFromDouble
import id.co.mka.narasource.meeting.R
import id.co.mka.narasource.meeting.meetingui.audio.MeetingAudioCallback
import id.co.mka.narasource.meeting.meetingui.audio.MeetingAudioHelper
import id.co.mka.narasource.meeting.meetingui.other.MeetingCommonCallback
import id.co.mka.narasource.meeting.meetingui.remotecontrol.MeetingRemoteControlHelper
import id.co.mka.narasource.meeting.meetingui.share.MeetingShareCallback
import id.co.mka.narasource.meeting.meetingui.share.MeetingShareHelper
import id.co.mka.narasource.meeting.meetingui.user.MeetingUserCallback
import id.co.mka.narasource.meeting.meetingui.video.MeetingVideoCallback
import id.co.mka.narasource.meeting.meetingui.video.MeetingVideoHelper
import id.co.mka.narasource.meeting.meetingui.view.MeetingOptionBar
import id.co.mka.narasource.meeting.meetingui.view.adapter.AttenderVideoAdapter
import id.co.mka.narasource.meeting.meetingui.view.share.AnnotateToolbar
import id.co.mka.narasource.meeting.meetingui.view.share.CustomShareView
import us.zoom.sdk.*
import us.zoom.sdk.IBOAttendeeEvent.ATTENDEE_REQUEST_FOR_HELP_RESULT

class MyMeetingActivity :
    AppCompatActivity(),
    MeetingVideoCallback.VideoEvent,
    MeetingAudioCallback.AudioEvent,
    MeetingShareCallback.ShareEvent,
    MeetingUserCallback.UserEvent,
    MeetingCommonCallback.CommonEvent {

    companion object {
        @kotlin.jvm.JvmField
        var REQUEST_SHARE_SCREEN_PERMISSION = 1001

        val JOIN_FROM_UNLOGIN = 1

        val JOIN_FROM_APIUSER = 2

        val JOIN_FROM_LOGIN = 3

        val REQUEST_STORAGE_CODE = 1012

        const val EXTRA_DATA = "meeting_data"
    }

    private val TAG = MyMeetingActivity::class.java.simpleName

    private val meetingViewModel: MeetingViewModel by viewModels()
    private lateinit var activity: Activity

    private lateinit var serviceTimerIntent: Intent
    private var timerStarted = false
    private var time = 0.0

    val REQUEST_CHAT_CODE = 1000
    val REQUEST_PLIST = 1001

    val REQUEST_CAMERA_CODE = 1010

    val REQUEST_AUDIO_CODE = 1011

    protected val REQUEST_SYSTEM_ALERT_WINDOW = 1002

    protected val REQUEST_SYSTEM_ALERT_WINDOW_FOR_MINIWINDOW = 1003

    protected val REQUEST_PHONE_STATUS_BLUETOOTH = 1004
    private var from = 0

    private var currentLayoutType = -1
    private val LAYOUT_TYPE_PREVIEW = 0
    private val LAYOUT_TYPE_WAITHOST = 1
    private val LAYOUT_TYPE_IN_WAIT_ROOM = 2
    private val LAYOUT_TYPE_ONLY_MYSELF = 3
    private val LAYOUT_TYPE_ONETOONE = 4
    private val LAYOUT_TYPE_LIST_VIDEO = 5
    private val LAYOUT_TYPE_VIEW_SHARE = 6
    private val LAYOUT_TYPE_SHARING_VIEW = 7
    private val LAYOUT_TYPE_WEBINAR_ATTENDEE = 8

    private lateinit var mWaitJoinView: View
    private lateinit var mWaitRoomView: View
    private lateinit var mConnectingText: TextView
    private lateinit var mBtnJoinBo: Button
    private lateinit var mBtnRequestHelp: Button

    private lateinit var videoListLayout: LinearLayout

    private var layout_lans: View? = null

    private var mMeetingFailed = false

    var mCurShareUserId: Long = -1

    private lateinit var mDefaultVideoView: MobileRTCVideoView
    private var mDefaultVideoViewMgr: MobileRTCVideoViewManager? = null

    private lateinit var meetingAudioHelper: MeetingAudioHelper

    private lateinit var meetingVideoHelper: MeetingVideoHelper

    private lateinit var meetingShareHelper: MeetingShareHelper

    private lateinit var remoteControlHelper: MeetingRemoteControlHelper

    private lateinit var mMeetingService: MeetingService

    private lateinit var mInMeetingService: InMeetingService

    private lateinit var smsService: SmsService

    private val mScreenInfoData: Intent? = null

    private lateinit var mShareView: MobileRTCShareView
    lateinit var mDrawingView: AnnotateToolbar
    private lateinit var mMeetingVideoView: FrameLayout

    private lateinit var mNormalSenceView: View

    private lateinit var customShareView: CustomShareView

    private lateinit var mVideoListView: RecyclerView

    private var mAdapter: AttenderVideoAdapter? = null

    private lateinit var meetingOptionBar: MeetingOptionBar

    private lateinit var gestureDetector: GestureDetector

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mMeetingService = ZoomSDK.getInstance().meetingService
        mInMeetingService = ZoomSDK.getInstance().inMeetingService

        if (null != intent.extras) {
            from = intent.extras!!.getInt("from")
        }
        activity = intent.getParcelableExtra<Activity>(EXTRA_DATA) as Activity
        meetingViewModel.activity = activity
        time = DateUtils.getRangeSeconds(activity.timeStart, activity.timeEnd).toDouble()
        meetingAudioHelper = MeetingAudioHelper(audioCallBack)
        meetingVideoHelper = MeetingVideoHelper(this, videoCallBack)

        registerListener()
        setContentView(R.layout.my_meeting_layout)

        gestureDetector = GestureDetector(this, gestureDetectorListener)

        meetingOptionBar = findViewById<View>(R.id.meeting_option_contain) as MeetingOptionBar
        meetingOptionBar.setCallBack(callBack)
        mMeetingVideoView = findViewById<View>(R.id.meetingVideoView) as FrameLayout
        mShareView = findViewById<View>(R.id.sharingView) as MobileRTCShareView
        mDrawingView = findViewById<View>(R.id.drawingView) as AnnotateToolbar
        mWaitJoinView = findViewById(R.id.waitJoinView)
        mWaitRoomView = findViewById(R.id.waitingRoom)

        val inflater = layoutInflater
        mNormalSenceView = inflater.inflate(R.layout.layout_meeting_content_normal, null)
        mDefaultVideoView = mNormalSenceView.findViewById(R.id.videoView)

        customShareView = mNormalSenceView.findViewById<View>(R.id.custom_share_view) as CustomShareView
        remoteControlHelper = MeetingRemoteControlHelper(customShareView)
        mMeetingVideoView.addView(
            mNormalSenceView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        mConnectingText = findViewById<View>(R.id.connectingTxt) as TextView
        mBtnJoinBo = findViewById<View>(R.id.btn_join_bo) as Button
        mBtnRequestHelp = findViewById(R.id.btn_request_help)

        @SuppressLint
        mVideoListView = findViewById<View>(R.id.videoList) as RecyclerView
        mVideoListView.bringToFront()

        videoListLayout = findViewById(R.id.videoListLayout)

        layout_lans = findViewById(R.id.layout_lans)

        mVideoListView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mAdapter = AttenderVideoAdapter(this, windowManager.defaultDisplay.width, pinVideoListener)
        mVideoListView.adapter = mAdapter

        refreshToolbar()
        setupBottomView()
    }
    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            updateTime(time)
            if (time == 0.0) {
                AlertDialog.Builder(this@MyMeetingActivity, id.co.mka.narasource.core.R.style.ThemeOverlay_App_MaterialAlertDialog).apply {
                    setTitle("Peringatan")
                    setMessage("Waktu meeting telah habis")
                    setCancelable(false)
                    setPositiveButton("OK") { _, _ ->
                        leave(true)
                    }
                    create()
                    show()
                }
            }
        }
    }
    private fun setupBottomView() {
        serviceTimerIntent = Intent(this, TimerService::class.java)
        registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))
        val meetingTitle = findViewById<TextView>(R.id.tv_meet_title)
        val meetingCategory = findViewById<TextView>(R.id.tv_meet_category)
        meetingViewModel.activity?.let {
            meetingTitle.text = it.title
            meetingCategory.text = it.category
        }
    }

    private fun updateTime(time: Double) {
        val meetingTime = findViewById<Chip>(R.id.chip_meeting_timer)
        meetingTime.text = getTimeStringFromDouble(time)
    }
    private fun startTimer() {
        serviceTimerIntent.putExtra(TimerService.TIME_EXTRA, time)
        startService(serviceTimerIntent)
        timerStarted = true
    }

    private fun stopTimer() {
        stopService(serviceTimerIntent)
        timerStarted = false
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    private var audioCallBack: MeetingAudioHelper.AudioCallBack = object :
        MeetingAudioHelper.AudioCallBack {
        override fun requestAudioPermission(): Boolean {
            if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this@MyMeetingActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQUEST_AUDIO_CODE
                )
                return false
            }
            return true
        }
        override fun updateAudioButton() {
            meetingOptionBar.updateAudioButton()
        }
    }

    private var videoCallBack: MeetingVideoHelper.VideoCallBack = object :
        MeetingVideoHelper.VideoCallBack {
        override fun requestVideoPermission() = checkVideoPermission()
        override fun showCameraList(popupWindow: PopupWindow?) {
            popupWindow!!.showAsDropDown(meetingOptionBar.switchCameraView, 0, 20)
        }
    }

    private var pinVideoListener: AttenderVideoAdapter.ItemClickListener = object :
        AttenderVideoAdapter.ItemClickListener {
        override fun onItemClick(view: View?, position: Int, userId: Long) {
            if (currentLayoutType == LAYOUT_TYPE_VIEW_SHARE || currentLayoutType == LAYOUT_TYPE_SHARING_VIEW) {
                return
            }
            mDefaultVideoViewMgr?.removeAllVideoUnits()
            val renderInfo = MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
            mDefaultVideoViewMgr?.addAttendeeVideoUnit(userId, renderInfo)
        }
    }

    private fun checkVideoPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_CODE
            )
            return false
        }
        return true
    }

    private fun registerListener() {
        smsService = ZoomSDK.getInstance().smsService
        MeetingAudioCallback.getInstance().addListener(this)
        MeetingVideoCallback.getInstance().addListener(this)
        MeetingShareCallback.getInstance().addListener(this)
        MeetingUserCallback.getInstance().addListener(this)
        MeetingCommonCallback.getInstance().addListener(this)
    }

    private val iboAttendeeEvent: IBOAttendeeEvent =
        object : IBOAttendeeEvent {
            override fun onHelpRequestHandleResultReceived(eResult: ATTENDEE_REQUEST_FOR_HELP_RESULT) {
                if (eResult == ATTENDEE_REQUEST_FOR_HELP_RESULT.RESULT_IGNORE) {
                    AlertDialog.Builder(this@MyMeetingActivity)
                        .setMessage("The host is currently helping others. Please try again later.")
                        .setCancelable(false)
                        .setPositiveButton(
                            "OK"
                        ) { dialog, which -> }.create().show()
                }
            }

            override fun onHostJoinedThisBOMeeting() {
                mBtnRequestHelp.visibility = View.GONE
            }

            override fun onHostLeaveThisBOMeeting() {
                mBtnRequestHelp.visibility = View.VISIBLE
            }
        }

    private val gestureDetectorListener: SimpleOnGestureListener =
        object : SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.d(TAG, "onSingleTapUp: ${mDrawingView.isAnnotationStarted} ${remoteControlHelper.isEnableRemoteControl}")
                Log.d(TAG, "onSingleTapUp: ${!meetingOptionBar.isShowing}")

                val orientation: Int = resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (videoListLayout.visibility == View.VISIBLE && (e.x >= videoListLayout.left || e.y <= meetingOptionBar.topBarHeight) || e.y >= meetingOptionBar.bottomBarTop) {
                        return true
                    }
                } else {
                    if (videoListLayout.visibility == View.VISIBLE && (e.y >= videoListLayout.top || e.y <= meetingOptionBar.topBarHeight) || e.y >= meetingOptionBar.bottomBarTop) {
                        return true
                    }
                }
                if (mMeetingService.meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING) {
                    meetingOptionBar.hideOrShowToolbar(meetingOptionBar.isShowing)
                }
                return true
            }
        }

    private val callBack: MeetingOptionBar.MeetingOptionBarCallBack =
        object : MeetingOptionBar.MeetingOptionBarCallBack {
            override fun onClickBack() {
                onClickMiniWindow()
            }

            override fun onClickSwitchCamera() {
                meetingVideoHelper.switchCamera()
            }

            override fun onClickLeave() {
                showLeaveMeetingDialog()
            }

            override fun onClickAudio() {
                meetingAudioHelper.switchAudio()
            }

            override fun onClickVideo() {
                meetingVideoHelper.switchVideo()
            }

            override fun onClickChats() {
                mInMeetingService.showZoomChatUI(
                    this@MyMeetingActivity,
                    REQUEST_CHAT_CODE
                )
            }

            override fun onClickPlist() {
                mInMeetingService.showZoomParticipantsUI(
                    this@MyMeetingActivity,
                    REQUEST_PLIST
                )
            }

            override fun onClickDisconnectAudio() {
                meetingAudioHelper.disconnectAudio()
            }

            override fun onClickSwitchLoudSpeaker() {
                meetingAudioHelper.switchLoudSpeaker()
            }

            override fun showMoreMenu(popupWindow: PopupWindow?) {
                popupWindow!!.showAtLocation(
                    meetingOptionBar.parent as View,
                    Gravity.BOTTOM or Gravity.RIGHT,
                    0,
                    150
                )
            }

            override fun onHidden(hidden: Boolean) {
                updateVideoListMargin(hidden)
            }
        }

    private fun onClickMiniWindow() {
        if (mMeetingService.meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING) {
            // stop share
            if (currentLayoutType == LAYOUT_TYPE_VIEW_SHARE) {
                mDefaultVideoViewMgr?.removeShareVideoUnit()
                currentLayoutType = -1
            }
            val userList = ZoomSDK.getInstance().inMeetingService.inMeetingUserList
            if (null == userList || userList.size < 2) {
                showLeaveMeetingDialog()
                return
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.packageName)
                )
                startActivityForResult(
                    intent,
                    REQUEST_SYSTEM_ALERT_WINDOW_FOR_MINIWINDOW
                )
            } else {
                showMainActivity()
            }
        } else {
            showLeaveMeetingDialog()
        }
    }
    private fun updateVideoListMargin(hidden: Boolean) {
        val params = videoListLayout.layoutParams as RelativeLayout.LayoutParams
        params.bottomMargin = if (hidden) 0 else meetingOptionBar.bottomBarHeight
        if (Configuration.ORIENTATION_LANDSCAPE == resources.configuration.orientation) {
            params.bottomMargin = 0
        }
        videoListLayout.layoutParams = params
        videoListLayout.bringToFront()
    }

    private fun showMainActivity() {
        finish()
    }

    private fun showLeaveMeetingDialog() {
        val builder = AlertDialog.Builder(this)
        if (mInMeetingService.isMeetingConnected) {
            if (mInMeetingService.isMeetingHost) {
                builder.setTitle("End or leave meeting")
                    .setPositiveButton(
                        "End"
                    ) { _, _ -> leave(true) }.setNeutralButton(
                        "Leave"
                    ) { _, _ -> leave(false) }
            } else {
                builder.setTitle("Leave meeting")
                    .setPositiveButton(
                        "Leave"
                    ) { _, _ -> leave(false) }
            }
        } else {
            builder.setTitle("Leave meeting")
                .setPositiveButton(
                    "Leave"
                ) { _, _ -> leave(false) }
        }
        if (mInMeetingService.inMeetingBOController.isInBOMeeting) {
            builder.setNegativeButton(
                "Leave BO"
            ) { _, _ -> leaveBo() }
        } else {
            builder.setNegativeButton("Cancel", null)
        }
        builder.create().show()
    }

    private fun showJoinFailDialog(error: Int) {
        val dialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("Meeting Fail")
            .setMessage("Error:$error")
            .setPositiveButton(
                "Ok"
            ) { _, _ -> finish() }.create()
        dialog.show()
    }

    private fun leave(end: Boolean) {
        stopTimer()
        mInMeetingService.leaveCurrentMeeting(end)
    }

    private fun leaveBo() {
        val boController = mInMeetingService.inMeetingBOController
        val iboAssistant = boController.boAssistantHelper
        if (iboAssistant != null) {
            iboAssistant.leaveBO()
        } else {
            val boAttendee = boController.boAttendeeHelper
            boAttendee?.leaveBo() ?: leave(false)
        }
    }

    private fun refreshToolbar() {
        if (mMeetingService.meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING) {
            mConnectingText.visibility = View.GONE
            meetingOptionBar.updateMeetingNumber("Meeting")
            meetingOptionBar.refreshToolbar()
        } else {
            if (mMeetingService.meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
                mConnectingText.visibility = View.VISIBLE
            } else {
                mConnectingText.visibility = View.GONE
            }
            meetingOptionBar.hideOrShowToolbar(true)
        }
    }

    private fun updateAnnotationBar() {
        if (mCurShareUserId > 0 && !isMySelfWebinarAttendee()) {
            if (meetingShareHelper.isSenderSupportAnnotation(mCurShareUserId)) {
                if (mInMeetingService.isMyself(mCurShareUserId) && !meetingShareHelper.isSharingScreen) {
                    if (meetingShareHelper.shareType === MeetingShareHelper.MENU_SHARE_SOURCE) {
                        mDrawingView.visibility = View.GONE
                    } else {
                        mDrawingView.visibility = View.VISIBLE
                    }
                } else {
                    if (currentLayoutType == LAYOUT_TYPE_VIEW_SHARE) {
                        mDrawingView.visibility = View.VISIBLE
                    } else {
                        mDrawingView.visibility = View.GONE
                    }
                }
            } else {
                mDrawingView.visibility = View.GONE
            }
        } else {
            mDrawingView.visibility = View.GONE
        }
    }

    private fun checkShowVideoLayout(forceRefresh: Boolean) {
        if (!checkVideoPermission()) {
            return
        }
        mDefaultVideoViewMgr = mDefaultVideoView.videoViewManager ?: null
        if (mDefaultVideoViewMgr != null) {
            val newLayoutType: Int = getNewVideoMeetingLayout()
            if (currentLayoutType != newLayoutType || newLayoutType == LAYOUT_TYPE_WEBINAR_ATTENDEE || forceRefresh) {
                removeOldLayout(currentLayoutType)
                currentLayoutType = newLayoutType
                addNewLayout(newLayoutType)
            }
        }
        updateAnnotationBar()
    }

    private fun getNewVideoMeetingLayout(): Int {
        var newLayoutType = -1
        if (mMeetingService.meetingStatus == MeetingStatus.MEETING_STATUS_WAITINGFORHOST) {
            newLayoutType = LAYOUT_TYPE_WAITHOST
            return newLayoutType
        }
        if (mMeetingService.meetingStatus == MeetingStatus.MEETING_STATUS_IN_WAITING_ROOM) {
            newLayoutType = LAYOUT_TYPE_IN_WAIT_ROOM
            return newLayoutType
        }
        if (mInMeetingService.isWebinarMeeting) {
            if (isMySelfWebinarAttendee()) {
                newLayoutType = LAYOUT_TYPE_WEBINAR_ATTENDEE
                return newLayoutType
            }
        }
        if (false) {
            newLayoutType = LAYOUT_TYPE_VIEW_SHARE
        } else if (false) {
            newLayoutType = LAYOUT_TYPE_SHARING_VIEW
        } else {
            val userlist = mInMeetingService.inMeetingUserList
            var userCount = 0
            if (userlist != null) {
                userCount = userlist.size
            }
            if (userCount > 1) {
                val preCount = userCount
                for (i in 0 until preCount) {
                    val userInfo = mInMeetingService.getUserInfoById(userlist!![i])
                    if (mInMeetingService.isWebinarMeeting) {
                        if (userInfo != null && userInfo.inMeetingUserRole == InMeetingUserInfo.InMeetingUserRole.USERROLE_ATTENDEE) {
                            userCount--
                        }
                    }
                }
            }
            newLayoutType = if (userCount == 0) {
                LAYOUT_TYPE_PREVIEW
            } else if (userCount == 1) {
                LAYOUT_TYPE_ONLY_MYSELF
            } else {
                LAYOUT_TYPE_LIST_VIDEO
            }
        }
        return newLayoutType
    }

    private fun removeOldLayout(type: Int) {
        if (type == LAYOUT_TYPE_WAITHOST) {
            mWaitJoinView.visibility = View.GONE
            mMeetingVideoView.visibility = View.VISIBLE
        } else if (type == LAYOUT_TYPE_IN_WAIT_ROOM) {
            mWaitRoomView.visibility = View.GONE
            mMeetingVideoView.visibility = View.VISIBLE
        } else if (type == LAYOUT_TYPE_PREVIEW || type == LAYOUT_TYPE_ONLY_MYSELF || type == LAYOUT_TYPE_ONETOONE) {
            mDefaultVideoViewMgr?.removeAllVideoUnits()
        } else if (type == LAYOUT_TYPE_LIST_VIDEO || type == LAYOUT_TYPE_VIEW_SHARE) {
            mDefaultVideoViewMgr?.removeAllVideoUnits()
            mDefaultVideoView.setGestureDetectorEnabled(false)
        } else if (type == LAYOUT_TYPE_SHARING_VIEW) {
            mShareView.visibility = View.GONE
            mMeetingVideoView.visibility = View.VISIBLE
        }
        if (type != LAYOUT_TYPE_SHARING_VIEW) {
            if (null != customShareView) {
                customShareView.visibility = View.INVISIBLE
            }
        }
    }

    private fun addNewLayout(type: Int) {
        if (type == LAYOUT_TYPE_WAITHOST) {
            mWaitJoinView.visibility = View.VISIBLE
            refreshToolbar()
            mMeetingVideoView.visibility = View.GONE
        } else if (type == LAYOUT_TYPE_IN_WAIT_ROOM) {
            mWaitRoomView.visibility = View.VISIBLE
            videoListLayout.visibility = View.GONE
            refreshToolbar()
            mMeetingVideoView.visibility = View.GONE
            mDrawingView.visibility = View.GONE
        } else if (type == LAYOUT_TYPE_PREVIEW) {
            showPreviewLayout()
        } else if (type == LAYOUT_TYPE_ONLY_MYSELF || type == LAYOUT_TYPE_WEBINAR_ATTENDEE) {
            showOnlyMeLayout()
        } else if (type == LAYOUT_TYPE_ONETOONE) {
            showOne2OneLayout()
        } else if (type == LAYOUT_TYPE_LIST_VIDEO) {
            showVideoListLayout()
        } else if (type == LAYOUT_TYPE_VIEW_SHARE) {
            showViewShareLayout()
        } else if (type == LAYOUT_TYPE_SHARING_VIEW) {
            showSharingViewOutLayout()
        }
    }

    private fun showPreviewLayout() {
        val renderInfo1 = MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
        mDefaultVideoView.visibility = View.VISIBLE
        mDefaultVideoViewMgr?.addPreviewVideoUnit(renderInfo1)
        videoListLayout.visibility = View.GONE
    }

    private fun showOnlyMeLayout() {
        mDefaultVideoView.visibility = View.VISIBLE
        videoListLayout.visibility = View.GONE
        val renderInfo = MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
        val myUserInfo = mInMeetingService.myUserInfo
        if (myUserInfo != null) {
            mDefaultVideoViewMgr?.removeAllVideoUnits()
            if (isMySelfWebinarAttendee()) {
                if (mCurShareUserId > 0) {
                    mDefaultVideoViewMgr?.addShareVideoUnit(
                        mCurShareUserId,
                        renderInfo
                    )
                } else {
                    mDefaultVideoViewMgr?.addActiveVideoUnit(renderInfo)
                }
            } else {
                mDefaultVideoViewMgr?.addAttendeeVideoUnit(myUserInfo.userId, renderInfo)
            }
        }
    }

    private fun showOne2OneLayout() {
        mDefaultVideoView.visibility = View.VISIBLE
        videoListLayout.visibility = View.VISIBLE
        val renderInfo = MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
        mDefaultVideoViewMgr?.addActiveVideoUnit(renderInfo)
        mAdapter!!.setUserList(mInMeetingService.inMeetingUserList)
        mAdapter!!.notifyDataSetChanged()
    }

    private fun showVideoListLayout() {
        val renderInfo = MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
        // options.aspect_mode = MobileRTCVideoUnitAspectMode.VIDEO_ASPECT_PAN_AND_SCAN;
        mDefaultVideoViewMgr?.addActiveVideoUnit(renderInfo)
        videoListLayout.visibility = View.VISIBLE
        updateAttendeeVideos(mInMeetingService.inMeetingUserList, 0)
    }

    private fun showSharingViewOutLayout() {
        if (meetingShareHelper.shareType === MeetingShareHelper.MENU_SHARE_SOURCE) {
            return
        }
        mAdapter!!.setUserList(null)
        mAdapter!!.notifyDataSetChanged()
        videoListLayout.visibility = View.GONE
        mMeetingVideoView.visibility = View.GONE
        mShareView.visibility = View.VISIBLE
    }

    private fun updateAttendeeVideos(userlist: List<Long>, action: Int) {
        when (action) {
            0 -> {
                mAdapter!!.setUserList(userlist)
                mAdapter!!.notifyDataSetChanged()
            }
            1 -> {
                mAdapter!!.addUserList(userlist)
            }
            else -> {
                val userId = mAdapter!!.selectedUserId
                if (userlist.contains(userId)) {
                    val inmeetingUserList = mInMeetingService.inMeetingUserList
                    if (inmeetingUserList.size > 0) {
                        mDefaultVideoViewMgr?.removeAllVideoUnits()
                        val renderInfo = MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
                        mDefaultVideoViewMgr?.addAttendeeVideoUnit(inmeetingUserList[0], renderInfo)
                    }
                }
                mAdapter!!.removeUserList(userlist)
            }
        }
    }

    private fun showViewShareLayout() {
        if (!isMySelfWebinarAttendee()) {
            mDefaultVideoView.visibility = View.VISIBLE
            mDefaultVideoView.setOnClickListener(null)
            mDefaultVideoView.setGestureDetectorEnabled(true)
            val shareUserId = mInMeetingService.activeShareUserID()
            val renderInfo1 = MobileRTCRenderInfo(0, 0, 100, 100)
            mDefaultVideoViewMgr?.addShareVideoUnit(shareUserId, renderInfo1)
            updateAttendeeVideos(mInMeetingService.inMeetingUserList, 0)
            customShareView.setMobileRTCVideoView(mDefaultVideoView)
            remoteControlHelper.refreshRemoteControlStatus()
        } else {
            mDefaultVideoView.visibility = View.VISIBLE
            mDefaultVideoView.setOnClickListener(null)
            mDefaultVideoView.setGestureDetectorEnabled(true)
            val shareUserId = mInMeetingService.activeShareUserID()
            val renderInfo1 = MobileRTCRenderInfo(0, 0, 100, 100)
            mDefaultVideoViewMgr?.addShareVideoUnit(shareUserId, renderInfo1)
        }
        mAdapter!!.setUserList(null)
        mAdapter!!.notifyDataSetChanged()
        videoListLayout.visibility = View.INVISIBLE
    }
    private fun isMySelfWebinarAttendee(): Boolean {
        val myUserInfo = mInMeetingService.myUserInfo
        return if (myUserInfo != null && mInMeetingService.isWebinarMeeting) {
            myUserInfo.inMeetingUserRole == InMeetingUserInfo.InMeetingUserRole.USERROLE_ATTENDEE
        } else false
    }

    private lateinit var builder: Dialog
    private fun showPsswordDialog(
        needPassword: Boolean,
        needDisplayName: Boolean,
        handler: InMeetingEventHandler
    ) {
        if (null != builder) {
            builder.dismiss()
        }
        builder = Dialog(this, us.zoom.videomeetings.R.style.ZMDialog)
        builder.setTitle("Need password or displayName")
        builder.setContentView(R.layout.layout_input_password_name)
        val pwd: EditText = builder.findViewById(R.id.edit_pwd)
        val name: EditText = builder.findViewById(R.id.edit_name)
        builder.findViewById<View>(R.id.layout_pwd).visibility = if (needPassword) View.VISIBLE else View.GONE
        builder.findViewById<View>(R.id.layout_name).visibility = if (needDisplayName) View.VISIBLE else View.GONE
        builder.findViewById<View>(R.id.btn_leave).setOnClickListener(
            View.OnClickListener {
                builder.dismiss()
                handler.setMeetingNamePassword("", "", true)
            }
        )
        builder.findViewById<View>(R.id.btn_ok).setOnClickListener(
            View.OnClickListener {
                val password = pwd.text.toString()
                val userName = name.text.toString()
                if (needPassword && TextUtils.isEmpty(password) || needDisplayName && TextUtils.isEmpty(
                        userName
                    )
                ) {
                    builder.dismiss()
                    onMeetingNeedPasswordOrDisplayName(needPassword, needDisplayName, handler)
                    return@OnClickListener
                }
                builder.dismiss()
                handler.setMeetingNamePassword(password, userName, false)
            }
        )
        builder.setCancelable(false)
        builder.setCanceledOnTouchOutside(false)
        builder.show()
        pwd.requestFocus()
    }

    private fun updateVideoView(userList: List<Long>, action: Int) {
        if (currentLayoutType == LAYOUT_TYPE_LIST_VIDEO || currentLayoutType == LAYOUT_TYPE_VIEW_SHARE) {
            if (mVideoListView.visibility == View.VISIBLE) {
                updateAttendeeVideos(userList, action)
            }
        }
    }

    private fun showEndOtherMeetingDialog(handler: InMeetingEventHandler) {
        val dialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("Meeting Alert")
            .setMessage("You have a meeting that is currently in-progress. Please end it to start a new meeting.")
            .setPositiveButton(
                "End Other Meeting"
            ) { _, _ -> handler.endOtherMeeting() }.setNeutralButton(
                "Leave"
            ) { _, _ ->
                finish()
                mInMeetingService.leaveCurrentMeeting(true)
            }.create()
        dialog.show()
    }

    private fun showWebinarNeedRegisterDialog(inMeetingEventHandler: InMeetingEventHandler?) {
        val dialog = AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("Need register to join this webinar meeting ")
            .setNegativeButton(
                "Cancel"
            ) { _, _ -> mInMeetingService.leaveCurrentMeeting(true) }
            .setPositiveButton("Ok") { _, _ ->
                if (null != inMeetingEventHandler) {
                    val time = System.currentTimeMillis()
                    inMeetingEventHandler.setRegisterWebinarInfo("test", "$time@example.com", false)
                }
            }.create()
        dialog.show()
    }

    override fun onUserAudioStatusChanged(userId: Long) {
        meetingAudioHelper.onUserAudioStatusChanged(userId)
    }

    override fun onUserAudioTypeChanged(userId: Long) {
        meetingAudioHelper.onUserAudioTypeChanged(userId)
    }

    override fun onMyAudioSourceTypeChanged(type: Int) {
        meetingAudioHelper.onMyAudioSourceTypeChanged(type)
    }

    override fun onPermissionRequested(permissions: Array<out String>?) {
        for (permission in permissions!!) {
            Log.d(TAG, "onPermissionRequested:$permission")
        }
    }

    override fun onWebinarNeedRegister(registerUrl: String?) {
        Log.d(TAG, "onWebinarNeedRegister: $registerUrl")
    }

    override fun onMeetingFail(errorCode: Int, internalErrorCode: Int) {
        mMeetingFailed = true
        mMeetingVideoView.visibility = View.GONE
        mConnectingText.visibility = View.GONE
        showJoinFailDialog(errorCode)
    }

    override fun onMeetingLeaveComplete(ret: Long) {
        if (!mMeetingFailed) finish()
    }

    override fun onMeetingStatusChanged(
        meetingStatus: MeetingStatus?,
        errorCode: Int,
        internalErrorCode: Int
    ) {
        checkShowVideoLayout(true)
        refreshToolbar()
        if (meetingStatus == MeetingStatus.MEETING_STATUS_FAILED) {
            mMeetingFailed = true
            mMeetingVideoView.visibility = View.GONE
            mConnectingText.visibility = View.GONE
            showJoinFailDialog(errorCode)
        } else if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING) {
            mMeetingFailed = false
            mMeetingVideoView.visibility = View.VISIBLE
            mConnectingText.visibility = View.GONE
            startTimer()
            Toast.makeText(this, "Meeting dimulai", Toast.LENGTH_SHORT).show()
        } else if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
            mMeetingFailed = false
            mMeetingVideoView.visibility = View.GONE
            mConnectingText.visibility = View.VISIBLE
        } else if (meetingStatus == MeetingStatus.MEETING_STATUS_DISCONNECTING) {
            mMeetingFailed = false
            mMeetingVideoView.visibility = View.GONE
            mConnectingText.visibility = View.VISIBLE
        } else if (meetingStatus == MeetingStatus.MEETING_STATUS_IDLE) {
            mMeetingFailed = false
            mMeetingVideoView.visibility = View.GONE
            mConnectingText.visibility = View.GONE
        } else if (meetingStatus == MeetingStatus.MEETING_STATUS_RECONNECTING) {
            if (!mMeetingFailed) finish()
        }
    }

    override fun onMeetingNeedPasswordOrDisplayName(
        needPassword: Boolean,
        needDisplayName: Boolean,
        handler: InMeetingEventHandler
    ) {
        showPsswordDialog(needPassword, needDisplayName, handler)
    }

    override fun onMeetingNeedColseOtherMeeting(inMeetingEventHandler: InMeetingEventHandler) {
        showEndOtherMeetingDialog(inMeetingEventHandler)
    }

    override fun onJoinWebinarNeedUserNameAndEmail(inMeetingEventHandler: InMeetingEventHandler?) {
        showWebinarNeedRegisterDialog(inMeetingEventHandler)
    }

    override fun onFreeMeetingReminder(
        isOrignalHost: Boolean,
        canUpgrade: Boolean,
        isFirstGift: Boolean
    ) {
        Log.d(TAG, "onFreeMeetingReminder: $isOrignalHost, $canUpgrade, $isFirstGift")
    }

    override fun onShareActiveUser(userId: Long) {
        Log.d(TAG, "onShareActiveUser: $userId")
    }

    override fun onShareUserReceivingStatus(userId: Long) {
        Log.d(TAG, "onShareUserReceivingStatus: $userId")
    }

    override fun onShareSettingTypeChanged(type: ShareSettingType?) {
        Log.d(TAG, "onShareSettingTypeChanged: $type")
    }

    override fun onMeetingUserJoin(userList: List<Long>) {
        Log.d(TAG, "onMeetingUserJoin: $userList")
        checkShowVideoLayout(true)
        updateVideoView(userList, 1)
    }

    override fun onMeetingUserLeave(userList: List<Long>) {
        checkShowVideoLayout(true)
        updateVideoView(userList, 2)
        refreshToolbar()
    }

    override fun onSilentModeChanged(inSilentMode: Boolean) {
        Log.d(TAG, "onSilentModeChanged: $inSilentMode")
    }

    override fun onUserVideoStatusChanged(userId: Long) {
        meetingOptionBar.updateVideoButton()
        meetingOptionBar.updateSwitchCameraButton()
    }
    override fun onBackPressed() {}
}
