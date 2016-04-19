package com.tiago.finalyearproject.model;

import java.io.Serializable;

/**
 * Created by Tiago on 22/03/2016.
 */
public class UserActivity implements Serializable{

    private String userId;
    private int activityId;
    private UserActivityState state;

    public enum UserActivityState {
        IN(1),
        OUT(2),
        OWNER(2);


        private final int num;

        private UserActivityState(int num)
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
     * @param activityId
     * @param state
     */
    public UserActivity(String userId, int activityId, UserActivityState state) {
        this.userId = userId;
        this.activityId = activityId;
        this.state = state;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public UserActivityState getState() {
        return state;
    }

    public void setState(UserActivityState state) {
        this.state = state;
    }


}
