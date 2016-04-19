/**
 * 
 */
package com.tiago.finalyearproject.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Tiago
 *
 */
public class AppActivity implements Serializable{


	private int activityId;
	private String name;
	private Date activityDateTime;
	private User activityOwner;
	private List<UserActivity> userActivityList;
	private ActivityType activityType;

	public AppActivity(int activityId, String name,Date activityDateTime, ActivityType activityType, User activityOwner, List<UserActivity> userActivityList) {
		this.activityId = activityId;
		this.name = name;
		this.activityDateTime = activityDateTime;
		this.activityType = activityType;
		this.activityOwner = activityOwner;
		this.userActivityList = userActivityList;

	}
	public AppActivity() {
	}

	public UserActivity.UserActivityState getCurrentUserActivityState(String id) {
		UserActivity.UserActivityState state = null;
		for (UserActivity userActivity: getUserActivityList()) {
			if((userActivity.getUserId()).equals(id)){
				state = userActivity.getState();
			}
		}
		return state;
	}


	public enum ActivityType {
		DRINKS,
		FOOD,
		SPORTS,
		BUSINESS,
		FILM,
		CLUB,
		OTHER;

		public static String[] names() {
			ActivityType[] states = values();
			String[] names = new String[states.length];

			for (int i = 0; i < states.length; i++) {
				names[i] = states[i].name();
			}

			return names;
		}
	}


	public int getActivityId() {
		return activityId;
	}


	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getActivityDateTime() {
		return activityDateTime;
	}


	public void setActivityDateTime(Date activityDateTime) {
		this.activityDateTime = activityDateTime;
	}



	public User getActivityOwner() {
		return activityOwner;
	}


	public void setActivityOwner(User activityOwner) {
		this.activityOwner = activityOwner;
	}


	public List<UserActivity> getUserActivityList() {
		return userActivityList;
	}


	public void setUserActivityList(List<UserActivity> userActivityList) {
		this.userActivityList = userActivityList;
	}



	public ActivityType getActivityType() {
		return activityType;
	}

	public void setActivity(ActivityType activityType) {
		this.activityType = activityType;
	}



}
