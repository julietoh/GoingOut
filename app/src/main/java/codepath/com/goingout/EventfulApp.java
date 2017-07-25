package codepath.com.goingout;

import android.app.Application;
import android.content.Context;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowLog;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by acamara on 7/23/17.
 */

class EventfulApp extends Application{
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        FlowManager.init(new FlowConfig.Builder(this).build());
        FlowLog.setMinimumLoggingLevel(FlowLog.Level.V);

        EventfulApp.context = this;
    }

    public static EventfulClient getRestClient() {
        return (EventfulClient) EventfulClient.getInstance(EventfulClient.class, EventfulApp.context);
    }
}
