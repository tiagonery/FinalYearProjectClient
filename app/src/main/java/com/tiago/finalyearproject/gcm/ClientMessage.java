/*
 * Copyright 2014 Wolfram Rittmeyer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tiago.finalyearproject.gcm;

import android.os.Parcelable;

import com.tiago.finalyearproject.model.Friendship;

import java.util.Map;


/**
 * Represents a message for CCS based massaging.
 */
public class ClientMessage extends AbstractMessage{


	public ClientMessage() {

	}

	public enum ClientMessageType{

		CREATE_USER, 
		DELETE_USER, 
		EDIT_CONFIG, 
		REQUEST_FRIENDSHIP_USERNAME, 
		REQUEST_FRIENDSHIP_FB, 
		ACCEPT_FRIENDSHIP, 
		REFUSE_FRIENDSHIP, 
		SEARCH_USER, 
		REMOVE_FRIEND, 
		CREATE_EVENT, 
		EDIT_EVENT, 
		INVITE_TO_EVENT, 
		ACCEPT_INVITE, 
		REFUSE_INVITE, 
		JOIN_EVENT, 
		LEAVE_EVENT, 
		GET_EVENTS, 
		WANT_TO_GO_OUT;

	}


	public enum ClientContentTypeKey {

		REG_ID;

	}
	
    /**
     * Recipient-ID.
     */
    private String from;

	/**
     * Sender app's package.
     */
    private ClientMessageType category;

    public ClientMessage(String from, ClientMessageType clientMessageType, String messageId, Map<String, Object> content) {
        super (messageId, content);
    	this.from = from;
        this.category = clientMessageType;
    }

    /**
	 * 
	 */

	public String getFrom() {
        return from;
    }

    public ClientMessageType getCategory() {
        return category;
    }


	public void setFrom(String from) {
		this.from = from;
	}

	public void setCategory(ClientMessageType category) {
		this.category = category;
	}

	/**
	 * @return
	 */
	public String getFacebookID() {
		String facebookId= (String) getContent().get("facebook_id");
		return facebookId;
	}

	/**
	 * @return
	 */
	public String getConfigValue() {
		String configValue= (String) getContent().get("configValue");
		return configValue;
	}

	/**
	 * @return
	 */
	public String getConfigKey() {
		String configKey = (String) getContent().get("config_key");
		return configKey;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		String username = (String) getContent().get("username");
		return username;
	}

	/**
	 * @return
	 */
	public int getInviteID() {
		int inviteID = (int) getContent().get("inviteID");
		return inviteID;
	}

	/**
	 * @return
	 */
	public int getEventID() {
		int eventID = (int) getContent().get("eventID");
		return eventID;
	}

	/**
	 * @return
	 */
	public String getName() {
		String name = (String) getContent().get("name");
		return name;
	}

	/**
	 * @return
	 */
	public String getSurname() {
		String surname = (String) getContent().get("surname");
		return surname;
	}

	/**
	 * @return
	 */
	public Friendship getFriendshipRequest() {
		Friendship friendship = (Friendship) getContent().get("friendship");
		return friendship;
	}

	public void setId(String regid) {
		getContent().put(ClientContentTypeKey.REG_ID.name(), regid);
	}

}