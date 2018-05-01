package com.acacho.ryanair.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Flight {
	private int number;
    private String departureTime;
    private String arrivalTime;
    
    public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}
	public String getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(String arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
    @Override
    public String toString() {
        return "Flight{" +
                "number='" + number + '\'' +
                "departureTime='" + departureTime + '\'' +
                "arrivalTime='" + arrivalTime + '\'' +
                '}';
    }

}
