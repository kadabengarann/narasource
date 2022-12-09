package id.co.mka.narasource.meeting.meetingui.audio;


import id.co.mka.narasource.meeting.meetingui.BaseCallback;
import id.co.mka.narasource.meeting.meetingui.BaseEvent;
import id.co.mka.narasource.meeting.meetingui.SimpleInMeetingListener;
import us.zoom.sdk.ZoomSDK;

public class MeetingAudioCallback extends BaseCallback<MeetingAudioCallback.AudioEvent> {

    public interface AudioEvent extends BaseEvent {

        void onUserAudioStatusChanged(long userId);

        void onUserAudioTypeChanged(long userId);

        void onMyAudioSourceTypeChanged(int type);
        void onPermissionRequested(String[] permissions);
    }

    static MeetingAudioCallback instance;

    private MeetingAudioCallback() {
        init();
    }

    protected void init() {
        ZoomSDK.getInstance().getInMeetingService().addListener(audioListener);
    }

    public static MeetingAudioCallback getInstance() {
        if (null == instance) {
            synchronized (MeetingAudioCallback.class) {
                if (null == instance) {
                    instance = new MeetingAudioCallback();
                }
            }
        }
        return instance;
    }


    SimpleInMeetingListener audioListener = new SimpleInMeetingListener() {
        @Override
        public void onPermissionRequested(String[] permissions) {
            for (AudioEvent event : callbacks) {
                event.onPermissionRequested(permissions);
            }
        }

        @Override
        public void onUserAudioStatusChanged(long userId, AudioStatus audioStatus) {
            for (AudioEvent event : callbacks) {
                event.onUserAudioStatusChanged(userId);
            }
        }

        @Override
        public void onUserAudioTypeChanged(long userId) {
            for (AudioEvent event : callbacks) {
                event.onUserAudioTypeChanged(userId);
            }
        }

        @Override
        public void onMyAudioSourceTypeChanged(int type) {
            for (AudioEvent event : callbacks) {
                event.onMyAudioSourceTypeChanged(type);
            }
        }
    };


}
