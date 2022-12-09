package id.co.mka.narasource.meeting.meetingui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.co.mka.narasource.core.domain.model.Activity
import javax.inject.Inject

@HiltViewModel
class MeetingViewModel @Inject constructor() : ViewModel() {
    var activity: Activity? = null
}
