package id.co.mka.narasource.meeting.meetingui.share;

import id.co.mka.narasource.meeting.meetingui.BaseCallback;
import id.co.mka.narasource.meeting.meetingui.BaseEvent;
import us.zoom.sdk.InMeetingShareController;
import us.zoom.sdk.ShareSettingType;
import us.zoom.sdk.SharingStatus;
import us.zoom.sdk.ZoomSDK;

public class MeetingShareCallback extends BaseCallback<MeetingShareCallback.ShareEvent> {

    public interface ShareEvent extends BaseEvent {

        void onShareActiveUser(long userId);

        void onShareUserReceivingStatus(long userId);

        void onShareSettingTypeChanged(ShareSettingType type);
    }

    static MeetingShareCallback instance;

    private MeetingShareCallback() {
        init();
    }


    protected void init() {
        ZoomSDK.getInstance().getInMeetingService().getInMeetingShareController().addListener(shareListener);
    }

    public static MeetingShareCallback getInstance() {
        if (null == instance) {
            synchronized (MeetingShareCallback.class) {
                if (null == instance) {
                    instance = new MeetingShareCallback();
                }
            }
        }
        return instance;
    }

    InMeetingShareController.InMeetingShareListener shareListener = new InMeetingShareController.InMeetingShareListener() {
        @Override
        public void onShareActiveUser(long userId) {

            for (ShareEvent event : callbacks) {
                event.onShareActiveUser(userId);
            }
        }

        @Override
        public void onSharingStatus(SharingStatus status, long userId) {

        }

        @Override
        public void onShareUserReceivingStatus(long userId) {

            for (ShareEvent event : callbacks) {
                event.onShareUserReceivingStatus(userId);
            }
        }

        @Override
        public void onShareSettingTypeChanged(ShareSettingType type) {
            for (ShareEvent event : callbacks) {
                event.onShareSettingTypeChanged(type);
            }
        }
    };

}
