package tysovsky.meet;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by tysovsky on 4/4/16.
 */
public class GCMMessageHandler extends GcmListenerService {
    public static Activity currentActivity;
    public static final Object CURRENTACTIVITYLOCK = new Object();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);
    }
}
