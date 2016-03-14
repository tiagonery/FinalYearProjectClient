package com.tiago.finalyearproject.gcm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tiago.finalyearproject.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONValue;

/**
 * Sample Smack implementation of a client for GCM Cloudpublic GCMManager() {
    } Connection Server.
 * Most of it has been taken more or less verbatim from Googles
 * documentation: http://developer.android.com/google/gcm/ccs.html
 * <br>
 * But some additions have been made. Bigger changes are annotated like that:
 * "/// new".
 * <br>
 * Those changes have to do with parsing certain type of messages
 * as well as with sending messages to a list of recipients. The original code
 * only covers sending one message to exactly one recipient.
 */
public class GCMManager {

    private GoogleCloudMessaging gcm;
    private String regid;
    private Context context;

    public GCMManager(Context context) {
        this.context = context;
    }




    public  void createRegId() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getContext());
                    }
                    if (regid == null) {
                        regid = gcm.register(Constants.PROJECT_ID);
                        msg = "Device registered, registration ID=" + regid;
                        Log.i("GCM", msg);
                    }
                    sendIDToGCM();

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
//                etRegId.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }

    public void sendIDToGCM() {

        if (getRegid() != null) {
            try {
                Bundle data = new Bundle();
                // the account is used for keeping
                // track of user notifications
                data.putString("account", getRegid());
                // the action is used to distinguish
                // different message types on the server
                data.putString("action", Constants.ACTION_REGISTER);
                String msgId = Integer.toString(getNextMsgId());
                gcm.send(Constants.PROJECT_ID + "@gcm.googleapis.com", msgId, Constants.GCM_DEFAULT_TTL, data);
            } catch (IOException e) {
                Log.e("gcmandroidtiago", "IOException while sending registration id", e);
            }
        }
    }

    private int getNextMsgId() {
        SharedPreferences prefs = getContext().getSharedPreferences(
                "com.example.app", Context.MODE_PRIVATE);
        int curMessageID = prefs.getInt(Constants.KEY_MSG_ID, 0);
        prefs.edit().putInt(Constants.KEY_MSG_ID, ++curMessageID).apply();
        return curMessageID;
    }


    /**
     * Creates a JSON encoded GCM message.
     *
     * @param to             RegistrationId of the target device (Required).
     * @param messageId      Unique messageId for which CCS will send an "ack/nack"
     *                       (Required).
     * @param payload        Message content intended for the application. (Optional).
     * @param collapseKey    GCM collapse_key parameter (Optional).
     * @param timeToLive     GCM time_to_live parameter (Optional).
     * @param delayWhileIdle GCM delay_while_idle parameter (Optional).
     * @return JSON encoded GCM message.
     */
    public static String createJsonMessage(String to, long messageId, Map<String, Object> payload,String collapseKey, Long timeToLive, Boolean delayWhileIdle) {
        return createJsonMessage(createAttributeMap(to, messageId, payload,
                collapseKey, timeToLive, delayWhileIdle));
    }

    public static String createJsonMessage(Map map) {
        return JSONValue.toJSONString(map);
    }

    public static Map createAttributeMap(String to, long messageId, Map<String, Object> payload,
                                         String collapseKey, Long timeToLive, Boolean delayWhileIdle) {
        Map<String, Object> message = new HashMap<String, Object>();
        if (to != null) {
            message.put("to", to);
        }
        if (collapseKey != null) {
            message.put("collapse_key", collapseKey);
        }
        if (timeToLive != null) {
            message.put("time_to_live", timeToLive);
        }
        if (delayWhileIdle != null && delayWhileIdle) {
            message.put("delay_while_idle", true);
        }
        message.put("message_id", messageId);
        message.put("data", payload);
        return message;
    }



    public String getRegid() {
        return regid;
    }

    public Context getContext() {
        return context;
    }

}


