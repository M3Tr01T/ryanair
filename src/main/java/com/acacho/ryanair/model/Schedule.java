package com.acacho.ryanair.model;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule {
	private int month;
	@JsonProperty("days")
	private Set<DayWithFlight> daysWithFlight;
	
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public Set<DayWithFlight> getDaysWithFlight() {
		return daysWithFlight;
	}
	public void setDaysWithFlight(Set<DayWithFlight> daysWithFlight) {
		this.daysWithFlight = daysWithFlight;
	}
	
    @Override
    public String toString() {
        return "Schedule{" +
                "month='" + month + '\'' +
                ", daysWithFlight=" + daysWithFlight.toString() +
                '}';
    }
	
}
