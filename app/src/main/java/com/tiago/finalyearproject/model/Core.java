/**
 * 
 */
package com.tiago.finalyearproject.model;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.tiago.finalyearproject.R;
import com.tiago.finalyearproject.gcm.ClientMessage;
import com.tiago.finalyearproject.gcm.GCMManager;
import com.tiago.finalyearproject.gcm.GcmMessageHandler;
import com.tiago.finalyearproject.gcm.ServerMessage;
import com.tiago.finalyearproject.view.AppAbstractFragmentActivity;
import com.tiago.finalyearproject.view.EventsFragment;
import com.tiago.finalyearproject.view.HomeActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Tiago
 *
 */
public class Core {

	private static Core core = new Core();
	private static GCMManager gcmManager;
	private AppAbstractFragmentActivity currentActivity;
	private List<ClientMessage> pendingClientMessages;


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
		Core.getInstance().handlePendingMessage(serverMessage);
	}

	public void notifyReplyError(ServerMessage serverMessage) {
		Core.getInstance().handlePendingMessage(serverMessage);
	}

	public void showFriendsRefreshButton() {
		if(getCurrentActivity()!=null && getCurrentActivity() instanceof HomeActivity){
			((HomeActivity)getCurrentActivity()).getFriendsFragament().setRefresh(true);
		}
	}

	public void notifyInvitationReceived() {
		if(getCurrentActivity()!=null && getCurrentActivity() instanceof HomeActivity){
			((HomeActivity)getCurrentActivity()).getEvetsFragament().setRefresh(true);
		}
	}
	public void showEventsRefreshButton() {
		if(getCurrentActivity()!=null && getCurrentActivity() instanceof HomeActivity){
			((HomeActivity)getCurrentActivity()).getEvetsFragament().setRefresh(true);
		}
	}
	public void showWishesRefreshButton() {
		if(getCurrentActivity()!=null && getCurrentActivity() instanceof HomeActivity){
			((HomeActivity)getCurrentActivity()).getWishesFragament().setRefresh(true);
		}
	}



	public List<ClientMessage> getPendingClientMessages() {
		if (pendingClientMessages == null){
			pendingClientMessages = new ArrayList<ClientMessage>();
		}
		return pendingClientMessages;
	}



	public void handlePendingMessage(final ServerMessage message){
		List<ClientMessage> pendingClientMessagesCopy = new ArrayList<>(getPendingClientMessages());
		for(Iterator<ClientMessage> iterator = pendingClientMessagesCopy.iterator(); iterator.hasNext();) {
			final ClientMessage clientMessage = iterator.next();
			if (message.getMessageRepliedId().equals(clientMessage.getMessageId())) {
				getPendingClientMessages().remove(clientMessage);


				final Handler mainHandler = new Handler(getCurrentActivity().getMainLooper());

				final Runnable myRunnable = new Runnable() {
					@Override
					public void run() {
						getCurrentActivity().handlePendingMessage(message, clientMessage.getMessageType());
					}
				};
				mainHandler.post(myRunnable);
			}
		}
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



