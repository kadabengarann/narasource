package id.co.mka.narasource.meeting

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import id.co.mka.narasource.core.domain.model.Activity
import id.co.mka.narasource.meeting.databinding.ActivityStartMeetBinding
import id.co.mka.narasource.meeting.meetingui.MyMeetingActivity
import us.zoom.sdk.*

class StartMeetActivity : AppCompatActivity(), MeetingServiceListener {

    private lateinit var binding: ActivityStartMeetBinding
    private lateinit var activity: Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartMeetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = intent.getParcelableExtra<Activity>("activity") as Activity
        initializeSdk(this)
    }

    private fun initializeSdk(context: Context) {
        val sdk = ZoomSDK.getInstance()
        val params = ZoomSDKInitParams().apply {
            appKey = BuildConfig.ZOOM_API_KEY // Retrieve your SDK key and enter it here
            appSecret = BuildConfig.ZOOM_API_SECRET // Retrieve your SDK secret and enter it here
            domain = "zoom.us"
            enableLog = true // Optional: enable logging for debugging
        }
        val listener = object : ZoomSDKInitializeListener {
            override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) {
                if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
                    Log.d("TAG", "onZoomSDKInitializeResult: $errorCode")
                } else {
                    val id = activity.meetingId
                    val password = activity.meetingPassword
                    if (id != null && password != null) {
                        joinMeeting(this@StartMeetActivity, id, password)
                    }
                }
            }
            override fun onZoomAuthIdentityExpired() = Unit
        }
        sdk.initialize(context, listener, params)
    }

    private fun joinMeeting(context: Context, meetingNumber: String, pw: String) {
        ZoomSDK.getInstance().meetingSettingsHelper.isCustomizedMeetingUIEnabled = true
        val options = JoinMeetingOptions()
        val params = JoinMeetingParams().apply {
            displayName = "User"
            meetingNo = meetingNumber
            password = pw
        }
        ZoomSDK.getInstance().meetingService.joinMeetingWithParams(
            this,
            params,
            options
        )
        showMeetingUi()
    }

    override fun onMeetingStatusChanged(meetingStatus: MeetingStatus?, p1: Int, p2: Int) {
        Log.d(ContentValues.TAG, "onMeetingStatusChanged: $meetingStatus: $p1: $p2")
        if (ZoomSDK.getInstance().meetingSettingsHelper.isCustomizedMeetingUIEnabled) {
            if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
                showMeetingUi()
            }
        }
    }

    override fun onMeetingParameterNotification(p0: MeetingParameter?) {
        Log.d(ContentValues.TAG, "onMeetingParameterNotification: $p0")
    }

    private fun showMeetingUi() {
        Log.d(ContentValues.TAG, "showMeetingUi: ${ZoomSDK.getInstance().meetingService.meetingStatus}")
        Log.d(ContentValues.TAG, "showMeetingUi: ${ZoomSDK.getInstance().meetingSettingsHelper.isCustomizedMeetingUIEnabled}")
        if (ZoomSDK.getInstance().meetingSettingsHelper.isCustomizedMeetingUIEnabled) {
            val intent = Intent(this, MyMeetingActivity::class.java)
            intent.putExtra("from", MyMeetingActivity.JOIN_FROM_UNLOGIN)
            intent.putExtra(MyMeetingActivity.EXTRA_DATA, activity)

            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            startActivity(intent)
            finish()
        }
    }
}
