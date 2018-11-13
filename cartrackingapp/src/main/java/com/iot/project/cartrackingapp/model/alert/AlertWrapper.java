package com.iot.project.cartrackingapp.model.alert;

public class AlertWrapper {

	// Populated using an enum with 4 values:
	// NONE, HIGH, MEDIUM, LOW
	private String alertLevel;
	private String alertReason;

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
