package id.co.mka.narasource.meeting.meetingui.user;

import java.util.List;

import id.co.mka.narasource.meeting.meetingui.BaseCallback;
import id.co.mka.narasource.meeting.meetingui.BaseEvent;
import id.co.mka.narasource.meeting.meetingui.SimpleInMeetingListener;
import us.zoom.sdk.ZoomSDK;

public class MeetingUserCallback extends BaseCallback<MeetingUserCallback.UserEvent> {

    public interface UserEvent extends BaseEvent {

        void onMeetingUserJoin(List<Long> list);

        void onMeetingUserLeave(List<Long> list);

        void onSilentModeChanged(boolean inSilentMode);
    }

    static MeetingUserCallback instance;

    private MeetingUserCallback() {
        init();
    }


    protected void init() {
        ZoomSDK.getInstance().getInMeetingService().addListener(userListener);
    }

    public static MeetingUserCallback getInstance() {
        if (null == instance) {
            synchronized (MeetingUserCallback.class) {
                if (null == instance) {
                    instance = new MeetingUserCallback();
                }
            }
        }
        return instance;
    }

    SimpleInMeetingListener userListener = new SimpleInMeetingListener() {


        @Override
        public void onMeetingUserJoin(List<Long> list) {

            for (UserEvent event : callbacks) {
                event.onMeetingUserJoin(list);
            }
        }

        @Override
        public void onMeetingUserLeave(List<Long> list) {
            for (UserEvent event : callbacks) {
                event.onMeetingUserLeave(list);
            }
        }

        @Override
        public void onSilentModeChanged(boolean inSilentMode) {
            for (UserEvent event : callbacks) {
                event.onSilentModeChanged(inSilentMode);
            }
        }

    };
}
