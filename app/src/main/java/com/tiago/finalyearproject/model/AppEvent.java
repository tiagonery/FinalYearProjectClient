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
//	private EventVisualizationPrivacy eventVisualizationPrivacy;
//	private EventMatchingPrivacy eventMatchingPrivacy;
//	private Venue venue;
	private EventType eventType;


	public enum EventType {
		FOOD(R.drawable.food_unselected,R.drawable.food),
		COFFEE(R.drawable.coffee_unselected,R.drawable.coffee),
		BAR(R.drawable.bar_unselected, R.drawable.bar),
		CLUB(R.drawable.party_unselected,R.drawable.party),
		FILM(R.drawable.movie_unselected,R.drawable.movie),
		GAME(R.drawable.game_unselected,R.drawable.game),
		SPORTS(R.drawable.sports_unselected,R.drawable.sports),
		EXERCISE(R.drawable.exercise_unselected,R.drawable.exercise),
		SWIM(R.drawable.swimming_unselected,R.drawable.swimming),
		NATURE(R.drawable.nature_unselected,R.drawable.nature),
		TRAVEL(R.drawable.travel_unselected,R.drawable.travel),
		SHOPPING(R.drawable.shopping_unselected,R.drawable.shopping),
		DATING(R.drawable.dating_unselected,R.drawable.dating),
		BUSINESS(R.drawable.business_unselected,R.drawable.business),
		OTHER(R.drawable.other_unselected,R.drawable.other);

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


//	public EventVisualizationPrivacy getEventVisualizationPrivacy() {
//		return eventVisualizationPrivacy;
//	}
//
//
//	public void setEventVisualizationPrivacy(EventVisualizationPrivacy eventVisualizationPrivacy) {
//		this.eventVisualizationPrivacy = eventVisualizationPrivacy;
//	}


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


//	public EventMatchingPrivacy getEventMatchingPrivacy() {
//		return eventMatchingPrivacy;
//	}
//
//	public void setEventMatchingPrivacy(EventMatchingPrivacy eventMatchingPrivacy) {
//		this.eventMatchingPrivacy = eventMatchingPrivacy;
//	}


	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
