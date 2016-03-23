/**
 * 
 */
package com.tiago.finalyearproject.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Tiago
 *
 */
public class AppEvent implements Serializable{


	private String eventId;
	private String name;
	private String eventDateTimeStart;
	private Timestamp eventDateTimeEnd;
	private Address address;
	private User eventOwner;
	private List<User> confirmedParticipants;
	private List<User> invitedUsers;
	private EventType eventType;
	private Venue venue;
	private EventActivity activity;
	private UserEvent.UserEventState state;

	public AppEvent(String name, EventActivity activity , UserEvent.UserEventState state) {
		this.name = name;
		this.activity = activity;
		this.state =state;
	}


	public enum EventType{
		PUBLIC,
		PUBLIC_FOR_FRIENDS,
		PRIVATE;
	}

	public enum EventActivity{
		DRINKS,
		FOOD,
		SPORTS,
		BUSINESS,
		FILM,
		CLUB,
		OTHER;
	}


	public String getEventId() {
		return eventId;
	}


	public void setEventId(String eventId) {
		this.eventId = eventId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getEventDateTimeStart() {
		return eventDateTimeStart;
	}


	public void setEventDateTimeStart(String eventDateTimeStart) {
		this.eventDateTimeStart = eventDateTimeStart;
	}


	public Timestamp getEventDateTimeEnd() {
		return eventDateTimeEnd;
	}


	public void setEventDateTimeEnd(Timestamp eventDateTimeEnd) {
		this.eventDateTimeEnd = eventDateTimeEnd;
	}


	public Address getAddress() {
		return address;
	}


	public void setAddress(Address address) {
		this.address = address;
	}


	public User getEventOwner() {
		return eventOwner;
	}


	public void setEventOwner(User eventOwner) {
		this.eventOwner = eventOwner;
	}


	public List<User> getConfirmedParticipants() {
		return confirmedParticipants;
	}


	public void setConfirmedParticipants(List<User> confirmedParticipants) {
		this.confirmedParticipants = confirmedParticipants;
	}


	public List<User> getInvitedUsers() {
		return invitedUsers;
	}


	public void setInvitedUsers(List<User> invitedUsers) {
		this.invitedUsers = invitedUsers;
	}


	public EventType getEventType() {
		return eventType;
	}


	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}


	public Venue getVenue() {
		return venue;
	}


	public void setVenue(Venue venue) {
		this.venue = venue;
	}

	public EventActivity getActivity() {
		return activity;
	}

	public void setActivity(EventActivity activity) {
		this.activity = activity;
	}


	public UserEvent.UserEventState getState() {
		return state;
	}

	public void setState(UserEvent.UserEventState state) {
		this.state = state;
	}

}
