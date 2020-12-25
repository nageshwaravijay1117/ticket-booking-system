package com.superops.booking.model;

import org.springframework.web.multipart.MultipartFile;

public class Patient {
	private String firstName;
	private String lastName;
	private String uid;
	private String dob;
	private String bloodType;
	private String diagnosis;
	private MultipartFile photo;
	private Boolean isVIP;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public MultipartFile getPhoto() {
		return photo;
	}

	public void setPhoto(MultipartFile photo) {
		this.photo = photo;
	}

	public Boolean getIsVIP() {
		return isVIP;
	}

	public void setIsVIP(Boolean isVIP) {
		this.isVIP = isVIP;
	}



}
