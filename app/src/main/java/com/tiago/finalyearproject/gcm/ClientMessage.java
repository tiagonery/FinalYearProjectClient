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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.Friendship;
import com.tiago.finalyearproject.model.User;
import com.tiago.finalyearproject.model.Wish;

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
		REQUEST_USERS_WISH_LIST,
		ACCEPT_FRIENDSHIP, 
		REFUSE_FRIENDSHIP, 
		SEARCH_USER, 
		REMOVE_FRIEND,
		CREATE_EVENT,
		CREATE_WISH,
		EDIT_EVENT, 
		INVITE_TO_EVENT, 
		ACCEPT_INVITE, 
		REFUSE_INVITE,
		JOIN_EVENT,
		JOIN_WISH,
		LEAVE_EVENT,
		REQUEST_EVENTS,
		REQUEST_WISHES,
		WANT_TO_GO_OUT;

	}


	public enum ClientContentTypeKey {

		MESSAGE_TYPE,
		REG_ID,
		FB_ID,
		INVITE_ID,
		EVENT,
		WISH,
		WISH_ID,
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

    public ClientMessage(String from, ClientMessageType clientMessageType, String messageId, Map<String, String> content) {
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
		getContent().put(ClientContentTypeKey.MESSAGE_TYPE.name(),messageType.name());
	}

	public void setEvent(AppEvent event) {
		String string = getJsonValueOf(event);
		getContent().put(ClientContentTypeKey.EVENT.name(), string);
	}

	public void setWish(Wish wish) {
		String string = getJsonValueOf(wish);
		getContent().put(ClientContentTypeKey.WISH.name(), string);
	}
	public void setWishId(int wishID) {
		getContent().put(ClientContentTypeKey.WISH_ID.name(), wishID+"");
	}


	public void setId(String regid) {
		getContent().put(ClientContentTypeKey.REG_ID.name(), regid);
	}

	public void setFacebookId(String fbId) {
		getContent().put(ClientContentTypeKey.FB_ID.name(), fbId);
	}

	public void setUserCreated(User user) {
		String string = getJsonValueOf(user);
		getContent().put(ClientContentTypeKey.USER_CREATED.name(), string);
	}

	public void setFacebookIdsList(List<String> idsList) {
		String string = getJsonValueOf(idsList);
		getContent().put(ClientContentTypeKey.FB_IDS_LIST.name(), string);
	}

	private String getJsonValueOf(Object object) {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = null;
		try {
			json = ow.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}

}
