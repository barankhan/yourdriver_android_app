package live.yourdriver.driver.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import live.yourdriver.driver.Activity.NotifActivity;

public class NotificationAlertReceiver extends BroadcastReceiver {
    public NotificationAlertReceiver(){

    }

    @Override
    public void onReceive(Context context, Intent intent) {
//            Log.e("Notification Recieved",intent.getStringExtra("message"));
            Intent i = new Intent(context, NotifActivity.class);
            i.putExtras(intent);
            context.startActivity(i);
    }
}
