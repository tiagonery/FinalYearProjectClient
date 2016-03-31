/**
 * 
 */
package com.tiago.finalyearproject.model;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;



/**
 * @author Tiago
 *
 */
@JsonIgnoreProperties(value = { "profilePicture" })
public class User implements Serializable{


	private String username;
	private String name;
	private String surname;
	private String regId;
	private String facebookId;
	private UserStatus userStatus;
	private int age;
	@JsonIgnore
	private transient Bitmap profilePicture;



	public enum UserStatus{
		ON(StringConstants.ON),
		OFF(StringConstants.OFF);
		
		private UserStatus(String message) {
			this.message = message;
		}

		private String message;

		public String getMessage() {
			return message;
		}
	
	}


	public User(String regId) {
		this.regId = regId;
	}

	public User() {
	}

	/**
	 * @param regId
	 * @param facebookId
	 * @param name
	 * @param surname
	 */
	public User(String regId, String facebookId, String name, String surname) {
		this(regId);
		setFacebookId(facebookId);
		setName(name);
		setSurname(surname);
	}

	public String generateFullName() {
		return getName()+" "+getSurname();
	}
	public String getUserName() {
		return username;
	}


	public void setUserName(String userName) {
		this.username = userName;
	}


	public String getName() {
		if(name!=null) {
			return name;
		}else{
			return "";
		}
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getRegId() {
		return regId;
	}


	public void setRegId(String regId) {
		this.regId = regId;
	}


	public UserStatus getUserStatus() {
		return userStatus;
	}


	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Bitmap getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(Bitmap profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSurname() {
		if(surname!=null) {
			return surname;
		}else{
			return "";
		}
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}


	@JsonIgnore
	public User getUserWithoutPrivInfo() {
		User result = this;
		result.setRegId(null);
		return result;
	}
	
}
