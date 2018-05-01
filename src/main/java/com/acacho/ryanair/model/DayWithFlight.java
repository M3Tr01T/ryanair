package com.acacho.ryanair.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DayWithFlight {
	private int day;
	private Set<Flight> flights;
	
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public Set<Flight> getFlights() {
		return flights;
	}
	public void setFlights(Set<Flight> flights) {
		this.flights = flights;
	}
	
    @Override
    public String toString() {
        return "DayWithFlight{" +
                "day='" + day + '\'' +
                ", flights=" + flights.toString() +
                '}';
    }

}
