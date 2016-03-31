/**
 * 
 */
package com.tiago.finalyearproject.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author Tiago
 *
 */
public class AppEvent implements Serializable{


	private int eventId;
	private String name;
	private Date eventDateTimeStart;
//	private Timestamp eventDateTimeEnd;
//	private Address address;
	private User eventOwner;
	private String location;
	private List<UserEvent> userEventList;
	private EventVisualizationPrivacy eventVisualizationPrivacy;
	private EventMatchingPrivacy eventMatchingPrivacy;
//	private Venue venue;
	private EventActivity activity;

	public AppEvent(String name, EventActivity activity) {
		this.name = name;
		this.activity = activity;

	}
	public AppEvent() {
	}

	public UserEvent.UserEventState getCurrentUserEventState(String id) {
		UserEvent.UserEventState state = null;
		for (UserEvent userEvent: getUserEventList()) {
			if((userEvent.getUserId()+"").equals(id)){
				state = userEvent.getState();
			}
		}
		return state;
	}


	public enum EventVisualizationPrivacy {
		INVTED_FRIENDS,
		ALL_FRIENDS;
	}

	public enum EventMatchingPrivacy {
		DISABLED,
		ENABLED_FOR_FRIENDS,
		ENABLE_PUBLIC;
	}

	public enum EventActivity{
		DRINKS,
		FOOD,
		SPORTS,
		BUSINESS,
		FILM,
		CLUB,
		OTHER;

		public static String[] names() {
			EventActivity[] states = values();
			String[] names = new String[states.length];

			for (int i = 0; i < states.length; i++) {
				names[i] = states[i].name();
			}

			return names;
		}
	}


	public int getEventId() {
		return eventId;
	}


	public void setEventId(int eventId) {
		this.eventId = eventId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getEventDateTimeStart() {
		return eventDateTimeStart;
	}


	public void setEventDateTimeStart(Date eventDateTimeStart) {
		this.eventDateTimeStart = eventDateTimeStart;
	}


//	public Timestamp getEventDateTimeEnd() {
//		return eventDateTimeEnd;
//	}
//
//
//	public void setEventDateTimeEnd(Timestamp eventDateTimeEnd) {
//		this.eventDateTimeEnd = eventDateTimeEnd;
//	}
//
//
//	public Address getAddress() {
//		return address;
//	}
//
//
//	public void setAddress(Address address) {
//		this.address = address;
//	}


	public User getEventOwner() {
		return eventOwner;
	}


	public void setEventOwner(User eventOwner) {
		this.eventOwner = eventOwner;
	}


	public List<UserEvent> getUserEventList() {
		return userEventList;
	}


	public void setUserEventList(List<UserEvent> userEventList) {
		this.userEventList = userEventList;
	}


	public EventVisualizationPrivacy getEventVisualizationPrivacy() {
		return eventVisualizationPrivacy;
	}


	public void setEventVisualizationPrivacy(EventVisualizationPrivacy eventVisualizationPrivacy) {
		this.eventVisualizationPrivacy = eventVisualizationPrivacy;
	}


//	public Venue getVenue() {
//		return venue;
//	}
//
//
//	public void setVenue(Venue venue) {
//		this.venue = venue;
//	}

	public EventActivity getActivity() {
		return activity;
	}

	public void setActivity(EventActivity activity) {
		this.activity = activity;
	}


	public EventMatchingPrivacy getEventMatchingPrivacy() {
		return eventMatchingPrivacy;
	}

	public void setEventMatchingPrivacy(EventMatchingPrivacy eventMatchingPrivacy) {
		this.eventMatchingPrivacy = eventMatchingPrivacy;
	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
}
