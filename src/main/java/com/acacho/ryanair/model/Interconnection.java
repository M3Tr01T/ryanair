package com.acacho.ryanair.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Interconnection {
	private int stops;
	private List<Leg> legs;
	
	public int getStops() {
		return stops;
	}

	public void setStops(int stops) {
		this.stops = stops;
	}

	public List<Leg> getLegs() {
		return legs;
	}

	public void setLegs(List<Leg> legs) {
		this.legs = legs;
	}

    @Override
    public String toString() {
        return "Interconnections{" +
                "stops='" + stops + '\'' +
                ", legs=" + legs.toString() +
                '}';
    }

}
