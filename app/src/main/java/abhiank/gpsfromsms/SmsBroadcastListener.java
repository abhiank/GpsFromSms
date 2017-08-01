package abhiank.gpsfromsms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import static abhiank.gpsfromsms.GpsLocationService.MESSAGE_FROM_INTENT_EXTRA;
import static abhiank.gpsfromsms.MainActivity.SHARED_PREF_SENT;

/**
 * Created by abhimanyuagrawal on 05/10/16.
 */

public class SmsBroadcastListener extends BroadcastReceiver {

    private SharedPreferences preferences;

    @Override
    public void onReceive(Context context, Intent intent) {

        preferences = context.getSharedPreferences(context.getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);

        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            //get the SMS message passed in
            Bundle bundle = intent.getExtras();
            SmsMessage[] msgs = null;
            String msg_from;
            if (bundle != null) {
                //Retrieve the SMS message received
                try {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for (int i = 0; i < msgs.length; i++) {
                        msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        String msgBody = msgs[i].getMessageBody();
                        Log.i("gpsfromsms", msg_from);

                        String pin = preferences.getString(MainActivity.SHARED_PREF_PIN, null);
                        if (pin != null && msgBody.toUpperCase().contains("LOC " + pin)) {
                            Intent gpsServiceIntent = new Intent(context, GpsLocationService.class);
                            gpsServiceIntent.putExtra(MESSAGE_FROM_INTENT_EXTRA, msg_from);
                            preferences.edit().putBoolean(SHARED_PREF_SENT, false).apply();
                            context.startService(gpsServiceIntent);
                        }
                    }
                } catch (Exception e) {
                    Log.d("Exception caught", e.getMessage());
                }
            }
        }
    }
}