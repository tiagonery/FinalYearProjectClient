/**
 * 
 */
package com.tiago.finalyearproject.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.GCMManager;
import com.tiago.finalyearproject.gcm.GcmMessageHandler;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.view.AppAbstractFragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author Tiago
 *
 */
public class Core {

	private static Core core = new Core();
	private static GCMManager gcmManager;
	private AppAbstractFragmentActivity currentActivity;


	public AppAbstractFragmentActivity getCurrentActivity() {
		return currentActivity;
	}

	public void setCurrentActivity(AppAbstractFragmentActivity currentActivity) {
		this.currentActivity = currentActivity;
	}


	public static GCMManager getGcmManager() {

		return gcmManager;
	}
	/* Static 'instance' method */
	public static Core getInstance() {
		return core;
	}

	public void notifyReplySuccesfull(ServerMessage serverMessage) {
	}

	public void notifyReplyError(ServerMessage serverMessage) {
	}

	public void notifyFriendshipRequestReceived(ServerMessage serverMessage) {
	}

	public void notifyInvitationReceived(ServerMessage serverMessage) {
	}




	/**
	 * @param clientRequestMessage
	 */
	public String sendRequest(AppAbstractFragmentActivity activity, ClientMessage clientRequestMessage) {
		String result=null;
		setCurrentActivity(activity);
		GcmMessageHandler gcmMessageHandler = new GcmMessageHandler(activity.getApplicationContext());
		try {
			result = gcmMessageHandler.sendMessageToServer(clientRequestMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}return result;

	}
}



