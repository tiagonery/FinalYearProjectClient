/**
 * 
 */
package com.tiago.finalyearproject.model;

import com.tiago.finalyearproject.R;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private EventType eventType;


	public enum EventType {
		DRINKS(R.drawable.drink, R.drawable.football),
		FOOD(R.drawable.football,R.drawable.drink),
		SPORTS(R.drawable.football,R.drawable.drink),
		BUSINESS(R.drawable.football,R.drawable.drink),
		FILM(R.drawable.football,R.drawable.drink),
		CLUB(R.drawable.football,R.drawable.drink),
		OTHER(R.drawable.football,R.drawable.drink);

		private final int selectedImage;
		private final int image;

		private static Map<Integer, EventType> map = new HashMap<Integer, EventType>();

		static {
			for (EventType eventType : EventType.values()) {
				map.put(eventType.image, eventType);
			}
		}

		public static EventType valueOf(int num) {
			return map.get(num);
		}

		private EventType(int image, int selectedImage)
		{
			this.image = image;
			this.selectedImage = selectedImage;
		}

		public int getImage()
		{
			return image;
		}
		public int getSelectedImage()
		{
			return selectedImage;
		}

		public static String[] names() {
			EventType[] states = values();
			String[] names = new String[states.length];

			for (int i = 0; i < states.length; i++) {
				names[i] = states[i].name();
			}

			return names;
		}
	}

	public AppEvent(String name, EventType eventType) {
		this.name = name;
		this.eventType = eventType;

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

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
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
