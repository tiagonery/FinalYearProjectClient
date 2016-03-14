package com.tiago.finalyearproject.gcm;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.internal.js;
import com.tiago.finalyearproject.Constants;
import com.tiago.finalyearproject.Constants.EventbusMessageType;
import com.tiago.finalyearproject.Constants.State;
import com.tiago.finalyearproject.MainActivity;
import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.model.Core;
import com.tiago.finalyearproject.model.User;

import de.greenrobot.event.EventBus;

import android.widget.Toast;

import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import com.tiago.finalyearproject.gcm.ServerMessage.*;

/**
 * Created by Tiago on 18/02/2016.
 */

public class GcmMessageHandler extends IntentService {

    private NotificationManager mNotificationManager;
    private String mSenderId = null;
    private android.os.Handler handler;
    private String message;
    private Context context;

    public GcmMessageHandler() {
        super("GcmIntentService");
    }

    public GcmMessageHandler(Context applicationContext) {
        super("GcmIntentService");
        context = applicationContext;
    }


    public void handleIncomingDataMessage(ServerMessage serverMessage) {
        if (serverMessage.getServerMessageType() != null) {

            Core core = Core.getInstance();

                switch (serverMessage.getServerMessageType()) {
                    case REPLY_SUCCES:
                        core.notifyReplySuccesfull(serverMessage);
                        break;
                    case REPLY_ERROR:
                        core.notifyReplyError(serverMessage);//not implemented
                        break;
                    case NOTIFY_FRIENDSHIP_REQUEST_RECEIVED:
                        core.notifyFriendshipRequestReceived(serverMessage); //not implemented
                        break;
                    case NOTIFY_INVITATION_RECEIVED:
                        core.notifyInvitationReceived(serverMessage); //not implemented
                        break;

                    default:
                        // Statements
                }
                // PayloadProcessor processor =
                // ProcessorFactory.getProcessor(msg.getContent().get("action"));
                // processor.handleMessage(msg);
            }

    }


    /**
     *
     */
    private ServerMessage getMessage(Map<String, Object> jsonObject) {
        String from = jsonObject.get("from").toString();

        // PackageName of the application that sent this message.
        ServerMessageType category = (ServerMessageType) jsonObject.get("message_type");

        // unique id of this message
        String messageId = (String) jsonObject.get("message_id");

        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) jsonObject.get("data");

        ServerMessage msg = new ServerMessage(from, category, messageId, payload);

        return msg;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        handler = new android.os.Handler();
//        mSenderId = getResources().getString(R.string.gcm_project_id);
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // action handling for actions of the activity
        String action = intent.getAction();
        Log.v("gcmandroidtiago", "action: " + action);
//        if (action.equals(Constants.ACTION_REGISTER)) {
//            register(gcm);
//        }
        if (action.equals(Constants.ACTION_UNREGISTER)) {
            unregister(gcm, intent);
        } else if (action.equals(Constants.ACTION_ECHO)) {
            sendMessage(gcm, intent);
        }

        // handling of stuff as described on
        // http://developer.android.com/google/gcm/client.html
        try {
            Bundle extras = intent.getExtras();
            // The getMessageType() intent parameter must be the intent you
            // received in your BroadcastReceiver.
            String messageType = gcm.getMessageType(intent);

            if (extras != null && !extras.isEmpty()) { // has effect of
                // unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that
             * GCM will be extended in the future with new message types, just
             * ignore any message types you're not interested in, or that you
             * don't recognize.
             */
                if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                    message = "Send error: " + extras.toString();
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                    message = "Deleted messages on server: "+ extras.toString();
                    // If it's a regular GCM message, do some work.
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                    // Post notification of received message.
                    String msg = extras.getString("Message");
                    if (TextUtils.isEmpty(msg)) {
                        msg = "empty message";
                    }else{
                        try {
                            Map<String, Object> jsonMap = (Map<String, Object>) JSONValue.parseWithException(msg);
                            ServerMessage serverMessage = getMessage(jsonMap);
                            handleIncomingDataMessage(serverMessage);


                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    message = msg;
                    Log.i("gcmandroidtiago", "Received: " + extras.toString()+ ", sent: " + msg);
                }
                showToast();
            }
        } finally {
            // Release the wake lock provided by the WakefulBroadcastReceiver.
            GcmBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void unregister(GoogleCloudMessaging gcm, Intent intent) {
        try {
            Log.v("grokingandroid", "about to unregister...");
            gcm.unregister();
            Log.v("gcmandroidtiago", "device unregistered");

            // Persist the regID - no need to register again.
            removeRegistrationId();
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_EVENT_TYPE,
                    EventbusMessageType.UNREGISTRATION_SUCCEEDED.ordinal());
            EventBus.getDefault().post(bundle);
        } catch (IOException e) {
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.

            // I simply notify the user:
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.KEY_EVENT_TYPE,
                    EventbusMessageType.UNREGISTRATION_FAILED.ordinal());
            EventBus.getDefault().post(bundle);
            Log.e("gcmandroidtiago", "Unregistration failed", e);
        }
    }

    private void myMethod(String regid, ClientMessage msg) {
        Log.v("gcmandroidtiago", "device registered: " + regid);
        msg.setId(regid);

//            sendRegistrationIdToBackend(gcm, regid);

        // Persist the regID - no need to register again.
        storeRegistrationId(regid);
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.KEY_EVENT_TYPE, EventbusMessageType.REGISTRATION_SUCCEEDED.ordinal());
        bundle.putString(Constants.KEY_REG_ID, regid);
        EventBus.getDefault().post(bundle);
    }


    private void register(final GoogleCloudMessaging gcm, final ClientMessage msg, final String msgId) {
        Log.v("grokingandroid", "about to register...");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    String regid = gcm.register(Constants.PROJECT_ID);
                    Log.v("gcmandroidtiago", "device registered: " + regid);
                    msg.setId(regid);
                    // Persist the regID - no need to register again.
                    storeRegistrationId(regid);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.KEY_EVENT_TYPE, EventbusMessageType.REGISTRATION_SUCCEEDED.ordinal());
//                    bundle.putString(Constants.KEY_REG_ID, regid);
                    EventBus.getDefault().post(bundle);
                    send(gcm, msg, msgId);
                } catch (IOException ex) {
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                    // I simply notify the user:
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.KEY_EVENT_TYPE, EventbusMessageType.REGISTRATION_FAILED.ordinal());
                    EventBus.getDefault().post(bundle);
                    Log.e("gcmandroidtiago", "Registration failed", ex);
                }
                return "";
            }
            @Override
            protected void onPostExecute(String msg) {
//                etRegId.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }

    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Log.i("gcmandroidtiago", "Saving regId to prefs: " + regId);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constants.KEY_REG_ID, regId);
        editor.putInt(Constants.KEY_STATE, State.REGISTERED.ordinal());
        editor.commit();
    }

    private void removeRegistrationId() {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Log.i("gcmandroidtiago", "Removing regId from prefs");
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constants.KEY_REG_ID);
        editor.putInt(Constants.KEY_STATE, State.UNREGISTERED.ordinal());
        editor.commit();
    }


    public String sendMessageToServer(ClientMessage msg) throws IOException {
        String msgId = String.valueOf(System.currentTimeMillis());
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(getContext());
        if (msg.getCategory() == ClientMessage.ClientMessageType.CREATE_USER) {
            register(gcm, msg, msgId);
//            GCMManager gcmManager = new GCMManager(getContext());
//            gcmManager.createRegId();
        }else{
            send(gcm, msg, msgId);
        }
        return msgId;
    }

    private void send(GoogleCloudMessaging gcm, ClientMessage message, String msgId) throws IOException {
        Bundle data = new Bundle();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message_type", message.getCategory());
        for (String key : message.getContent().keySet()) {
            map.put(key, message.getContent().get(key));
        }
        data.putString("json", JSONValue.toJSONString(map));
        gcm.send(Constants.PROJECT_ID + "@gcm.googleapis.com", msgId, Constants.GCM_DEFAULT_TTL, data);
    }

    private String sendRegistrationIdToBackend(GoogleCloudMessaging gcm, String regId) {
        String msgId = String.valueOf(System.currentTimeMillis());

        try {
            Bundle data = new Bundle();
            // the name is used for keeping track of user notifications
            // if you use the same name everywhere, the notifications will
            // be cancelled
            data.putString("message_type", ClientMessage.ClientMessageType.CREATE_USER.toString());
//            data.putString("account", account);
            data.putString("id", regId);
            data.putString("action", Constants.ACTION_REGISTER);
//            String msgId = Integer.toString(getNextMsgId());
            gcm.send(mSenderId + "@gcm.googleapis.com", msgId,Constants.GCM_DEFAULT_TTL, data);
            Log.v("gcmandroidtiago", "regId sent: " + regId);
        } catch (IOException e) {
            Log.e("gcmandroidtiago",
                    "IOException while sending registration to backend...", e);
        }return msgId;
    }

    private void sendMessage(GoogleCloudMessaging gcm, Intent intent) {
        try {
            String msg = intent.getStringExtra(Constants.KEY_MESSAGE_TXT);
            Bundle data = new Bundle();
            data.putString(Constants.ACTION, Constants.ACTION_ECHO);
            data.putString("message", msg);
            String id = Integer.toString(getNextMsgId());
            gcm.send(mSenderId + "@gcm.googleapis.com", id, data);
            Log.v("gcmandroidtiago", "sent message: " + msg);
        } catch (IOException e) {
            Log.e("gcmandroidtiago", "Error while sending a message", e);
        }
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Constants.NOTIFICATION_ACTION);
        notificationIntent.putExtra(Constants.KEY_MESSAGE_TXT, msg);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.drawable.ic_stat_collections_cloud)
                .setContentTitle("GCM Notification")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(Constants.NOTIFICATION_NR, mBuilder.build());
    }

    private int getNextMsgId() {
        SharedPreferences prefs = getPrefs();
        int id = prefs.getInt(Constants.KEY_MSG_ID, 0);
        Editor editor = prefs.edit();
        editor.putInt(Constants.KEY_MSG_ID, ++id);
        editor.commit();
        return id;
    }

    private SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void showToast() {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    public Context getContext() {
        return context;
    }
}