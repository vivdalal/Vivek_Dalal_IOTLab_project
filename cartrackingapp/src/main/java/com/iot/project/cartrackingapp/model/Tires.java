package com.iot.project.cartrackingapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Tires {

	@JsonProperty("frontLeft")
	private int frontLeft;

	@JsonProperty("frontRight")
	private int frontRight;

	@JsonProperty("rearLeft")
	private int rearLeft;

	@JsonProperty("rearRight")
	private int rearRight;

//	public Tires(int frontLeft, int frontRight, int rearLeft, int rearRight) {
//		//super();
//		this.frontLeft = frontLeft;
//		this.frontRight = frontRight;
//		this.rearLeft = rearLeft;
//		this.rearRight = rearRight;
//	}

	public int getFrontLeft() {
		return frontLeft;
	}

	public void setFrontLeft(int frontLeft) {
		this.frontLeft = frontLeft;
	}

	public int getFrontRight() {
		return frontRight;
	}

	public void setFrontRight(int frontRight) {
		this.frontRight = frontRight;
	}

	public int getRearLeft() {
		return rearLeft;
	}

	public void setRearLeft(int rearLeft) {
		this.rearLeft = rearLeft;
	}

	public int getRearRight() {
		return rearRight;
	}

	public void setRearRight(int rearRight) {
		this.rearRight = rearRight;
	}

	@Override
	public String toString() {
		return "Tires [frontLeft=" + frontLeft + ", frontRight=" + frontRight + ", rearLeft=" + rearLeft
				+ ", rearRight=" + rearRight + "]";
	}

}
