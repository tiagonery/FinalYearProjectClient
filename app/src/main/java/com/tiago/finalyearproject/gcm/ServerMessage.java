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

import com.tiago.finalyearproject.model.AppEvent;
import com.tiago.finalyearproject.model.User;

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
        NOTIFY_INVITATION_RECEIVED;

    }

    public enum ServerContentTypeKey {

        MESSAGE_TYPE,
        MESSAGE_REPLIED_ID,
        ERROR_MESSAGE,
        FRIENDSHIP_REQUEST_FROM,
        FRIENDSHIP_REQUEST_ACCEPTED_FROM,
        EVENTS_LIST,
        EVENT_ID,
        EVENT_NAME,
        EVENT_DATE,
        EVENT_CATEGORY,
        EVENT_LOCATION,
        USER_EVENT_STATUS;

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



    public List<AppEvent> getEventsList() {
        List<AppEvent> list = new ArrayList<AppEvent>();
//        List<Map<String, Object>> listOfMaps = (( List<Map<String, Object>>) getContent().get(ServerContentTypeKey.EVENTS_LIST.name()));
//        for (Map<String, Object> eventMap: listOfMaps) {
//            AppEvent event = new AppEvent();
//            event.setEventId((String) eventMap.get(ServerContentTypeKey.EVENT_ID.name()));
//            event.setName((String) eventMap.get(ServerContentTypeKey.EVENT_NAME.name()));
////            event.setEventDateTimeStart((String) eventMap.get(ServerContentTypeKey.EVENT_DATE.name()));
//            event.setEventType(AppEvent.EventType.valueOf((String) eventMap.get(ServerContentTypeKey.EVENT_CATEGORY.name())));
////            event.setAddress((String)eventMap.get(ServerContentTypeKey.EVENT_LOCATION.name()));
//            list.add(event);
//        }
//
        return list;
    }

}
