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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.User;
import com.tiago.finalyearproject.model.Wish;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Represents a message for CCS based massaging.
 */
public class ServerMessage extends AbstractMessage {


    public ServerMessage() {

    }



    public enum ServerMessageType {

        REPLY_SUCCES,
        REPLY_ERROR,
        NOTIFY_FRIENDSHIP_REQUEST_RECEIVED,
        NOTIFY_INVITATION_RECEIVED,
        NOTIFY_NEW_EVENTAVAILABLE,
        NOTIFY_NEW_WISH_AVAILABLE;

    }

    public enum ServerContentTypeKey {

        MESSAGE_TYPE,
        MESSAGE_REPLIED_ID,
        ERROR_MESSAGE,
        FRIENDSHIP_REQUEST_FROM,
        FRIENDSHIP_REQUEST_ACCEPTED_FROM,
        EVENTS_LIST,
        EVENT,
        WISHES_LIST,
        WISH;

    }


    /**
     * Recipient-ID.
     */
    private String to;

    /**
     * Sender app's package.
     */
    private ServerMessageType serverMessageType;



    /**
     * @param to
     */
    public ServerMessage(String to) {

        this.to = to;
    }

    public ServerMessage(ServerMessageType serverMessageType, Map<String, String> jsonObject) {
        this.serverMessageType = serverMessageType;
        this.content = jsonObject;
    }

    /**
     *
     */

    public String getFrom() {

        return to;
    }

    public ServerMessageType getServerMessageType() {
        return serverMessageType;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessageRepliedId() {

        return (String) getContent().get(ServerContentTypeKey.MESSAGE_REPLIED_ID.name());
    }


    public String getMessageError() {
        return (String) getContent().get(ServerContentTypeKey.ERROR_MESSAGE.name());
    }



    /**
     * @param string
     */
    public void setErrorMessage(String string) {
        getContent().put(ServerContentTypeKey.ERROR_MESSAGE.name(), string);

    }

    /**
     * @param requesterFbId
     */
    public void setFriendshipRequester(String requesterFbId) {
        getContent().put(ServerContentTypeKey.FRIENDSHIP_REQUEST_FROM.name(), requesterFbId);

    }

    /**
     * @param userFbId
     */
    public void setFriendshipRequestAcceptedFrom(String userFbId) {
        getContent().put(ServerContentTypeKey.FRIENDSHIP_REQUEST_ACCEPTED_FROM.name(), userFbId);
    }






    public AppEvent getEvent() {
        String json = (String) getContent().get(ServerContentTypeKey.EVENT.name());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        AppEvent event = null;
        try {
            event = mapper.readValue(json, AppEvent.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return event;
    }

    public List<AppEvent> getEventsList() {
        String json = (String) getContent().get(ServerContentTypeKey.EVENTS_LIST.name());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        List<AppEvent> eventsList = null;
        try {
            eventsList = mapper.readValue(json, new TypeReference<List<AppEvent>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventsList;
    }

    public List<Wish> getWishesList() {
        String json = (String) getContent().get(ServerContentTypeKey.WISHES_LIST.name());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        List<Wish> eventsList = null;
        try {
            eventsList = mapper.readValue(json, new TypeReference<List<Wish>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return eventsList;
    }

}
