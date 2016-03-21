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

import com.tiago.finalyearproject.model.Friendship;
import com.tiago.finalyearproject.model.User;

import java.util.List;
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

		MESSAGE_TYPE,
		REG_ID,
		FB_ID,
		INVITE_ID,
		EVENT_ID,
		USER_NAME,
		USER_SURNAME,
		FRIENDSHIP,
		USER_CREATED,
		FB_IDS_LIST;

	}
	
    /**
     * Recipient-ID.
     */
    private String from;

	/**
     * Sender app's package.
     */
    private ClientMessageType messageType;

    public ClientMessage(String from, ClientMessageType clientMessageType, String messageId, Map<String, Object> content) {
        super (messageId, content);
    	this.from = from;
        this.messageType = clientMessageType;
    }

    /**
	 * 
	 */

	public String getFrom() {
        return from;
    }

    public ClientMessageType getMessageType() {
        return messageType;
    }


	public void setFrom(String from) {
		this.from = from;
	}

	public void setMessageType(ClientMessageType messageType) {
		this.messageType = messageType;
		getContent().put(ClientContentTypeKey.MESSAGE_TYPE.name(),messageType);
	}

	/**
	 * @return
	 */
	public String getFacebookID() {
		String facebookId= (String) getContent().get(ClientContentTypeKey.FB_ID.name());
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
		int inviteID = (int) getContent().get(ClientContentTypeKey.INVITE_ID.name());
		return inviteID;
	}

	/**
	 * @return
	 */
	public int getEventID() {
		int eventID = (int) getContent().get(ClientContentTypeKey.EVENT_ID.name());
		return eventID;
	}

	/**
	 * @return
	 */
	public String getName() {
		String name = (String) getContent().get(ClientContentTypeKey.USER_NAME.name());
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
		Friendship friendship = (Friendship) getContent().get(ClientContentTypeKey.FRIENDSHIP.name());
		return friendship;
	}

	public void setId(String regid) {
		getContent().put(ClientContentTypeKey.REG_ID.name(), regid);
	}

	public void setUserName(String name) {
		getContent().put(ClientContentTypeKey.USER_NAME.name(), name);
	}

	public void setUserSurname(String suname) {
		getContent().put(ClientContentTypeKey.USER_SURNAME.name(), suname);
	}

	public void setFacebookId(String fbId) {
		getContent().put(ClientContentTypeKey.FB_ID.name(), fbId);
	}

	public void setFacebookIdsList(List<String> fbIdsList) {
		getContent().put(ClientContentTypeKey.FB_IDS_LIST.name(), fbIdsList);
	}

	public void setUserCreated(User user) {
		getContent().put(ClientContentTypeKey.USER_CREATED.name(), user);
	}

	public User getUserCreated() {
		User user = (User) getContent().get(ClientContentTypeKey.USER_CREATED.name());
		return user;
	}

}
