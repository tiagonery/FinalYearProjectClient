package com.tiago.finalyearproject.model;

import java.io.Serializable;

/**
 * Created by Tiago on 22/03/2016.
 */
public class UserEvent implements Serializable{

    private String userId;
    private String eventId;
    private UserEventState state;

    public enum UserEventState {
        INVITED(1),
        GOING(2),
        NOT_GOING(3),
        IDLE(4),
        OWNER(5);


        private final int num;

        private UserEventState(int num)
        {
            this.num = num;
        }

        public int getNumber()
        {
            return num;
        }
    }

    /**
     * @param userId
     * @param eventId
     * @param state
     */
    public UserEvent(String userId, String eventId, UserEventState state) {
        this.userId = userId;
        this.eventId = eventId;
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public UserEventState getState() {
        return state;
    }

    public void setState(UserEventState state) {
        this.state = state;
    }


}
