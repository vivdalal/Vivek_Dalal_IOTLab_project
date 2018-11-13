package com.iot.project.cartrackingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SensorReading {

	@JsonProperty("vin")
	private String vin;

	@JsonProperty("latitude")
	private double latitude;

	@JsonProperty("longitude")
	private double longitude;

	@JsonProperty("timestamp")
	private String timestamp;

	@JsonProperty("fuelVolume")
	private double fuelVolume;

	@JsonProperty("speed")
	private int speed;

	@JsonProperty("engineHp")
	private int engineHp;

	@JsonProperty("checkEngineLightOn")
	private boolean checkEngineLightOn;

	@JsonProperty("engineCoolantLow")
	private boolean engineCoolantLow;

	@JsonProperty("cruiseControlOn")
	private boolean cruiseControlOn;

	@JsonProperty("engineRpm")
	private int engineRpm;

	@JsonProperty("tires")
	private Tires tires;

	@JsonProperty("alertLevel")
	private String alertLevel;

	@JsonProperty("alertReason")
	private String alertReason;

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public double getFuelVolume() {
		return fuelVolume;
	}

	public void setFuelVolume(double fuelVolume) {
		this.fuelVolume = fuelVolume;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getEngineHp() {
		return engineHp;
	}

	public void setEngineHp(int engineHp) {
		this.engineHp = engineHp;
	}

	public boolean isCheckEngineLightOn() {
		return checkEngineLightOn;
	}

	public boolean isEngineCoolantLow() {
		return engineCoolantLow;
	}

	public void setEngineCoolantLow(boolean engineCoolantLow) {
		this.engineCoolantLow = engineCoolantLow;
	}

	public void setCheckEngineLightOn(boolean checkEngineLightOn) {
		this.checkEngineLightOn = checkEngineLightOn;
	}

	public boolean isCruiseControlOn() {
		return cruiseControlOn;
	}

	public void setCruiseControlOn(boolean cruiseControlOn) {
		this.cruiseControlOn = cruiseControlOn;
	}

	public int getEngineRpm() {
		return engineRpm;
	}

	public void setEngineRpm(int engineRpm) {
		this.engineRpm = engineRpm;
	}

	public Tires getTires() {
		return tires;
	}

	public void setTires(Tires tires) {
		this.tires = tires;
	}

	public String getAlertLevel() {
		return alertLevel;
	}

	public void setAlertLevel(String alertLevel) {
		this.alertLevel = alertLevel;
	}

	public String getAlertReason() {
		return alertReason;
	}

	public void setAlertReason(String alertReason) {
		this.alertReason = alertReason;
	}

}
