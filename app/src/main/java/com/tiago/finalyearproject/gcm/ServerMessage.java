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
import com.tiago.finalyearproject.model.Friendship;
import com.tiago.finalyearproject.model.User;
import com.tiago.finalyearproject.model.UserEvent;
import com.tiago.finalyearproject.model.UserWish;
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
        NOTIFY_FRIENDSHIP_REQUEST_ACCEPTED,
        NOTIFY_FRIENDSHIP_REQUEST_REFUSED,
        NOTIFY_INVITATION_RECEIVED,
        NOTIFY_NEW_EVENTAVAILABLE,
        NOTIFY_NEW_WISH_AVAILABLE,
        NOTIFY_WISH_DELETED,
        NOTIFY_EVENT_DELETED;

    }

    public enum ServerContentTypeKey {

        MESSAGE_TYPE,
        MESSAGE_REPLIED_ID,
        ERROR_MESSAGE,
        FRIENDSHIP_REQUEST_FROM,
        FRIENDSHIP_REQUEST_ACCEPTED_FROM,
        FRIENDSHIP_LIST,
        EVENTS_LIST,
        EVENT,
        USERS_LIST,
        USERS_IDS_LIST,
        USERS_EVENT_LIST,
        WISHES_LIST,
        USERS_WISH_LIST,
        USERS_WISH,
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
    public Wish getWish() {
        String json = (String) getContent().get(ServerContentTypeKey.WISH.name());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        Wish wish = null;
        try {
            wish = mapper.readValue(json, Wish.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wish;
    }
    public UserWish getUserWish() {
        String json = (String) getContent().get(ServerContentTypeKey.USERS_WISH.name());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        UserWish userWish = null;
        try {
            userWish = mapper.readValue(json, UserWish.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userWish;
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

    public List<String> getUsersIdsList() {
        String json = (String) getContent().get(ServerContentTypeKey.USERS_IDS_LIST.name());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        List<String> usersIdsList = null;
        try {
            usersIdsList = mapper.readValue(json, new TypeReference<List<String>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usersIdsList;
    }

    public List<UserWish> getUsersWishList() {
        String json = (String) getContent().get(ServerContentTypeKey.USERS_WISH_LIST.name());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        List<UserWish> userWishList = null;
        try {
            userWishList = mapper.readValue(json, new TypeReference<List<UserWish>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userWishList;
    }

    public List<User> getUsersList() {
        String json = (String) getContent().get(ServerContentTypeKey.USERS_LIST.name());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        List<User> usersList = null;
        try {
            usersList = mapper.readValue(json, new TypeReference<List<User>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    public List<Friendship> getFriendshipList() {
        String json = (String) getContent().get(ServerContentTypeKey.FRIENDSHIP_LIST.name());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        List<Friendship> friendshipList = null;
        try {
            friendshipList = mapper.readValue(json, new TypeReference<List<Friendship>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return friendshipList;
    }

    public List<UserEvent> getUsersEventList() {
        String json = (String) getContent().get(ServerContentTypeKey.USERS_EVENT_LIST.name());
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(MapperFeature.AUTO_DETECT_FIELDS, true);
        List<UserEvent> userEventList = null;
        try {
            userEventList = mapper.readValue(json, new TypeReference<List<UserEvent>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userEventList;
    }

}
