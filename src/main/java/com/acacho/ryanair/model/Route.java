package com.acacho.ryanair.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {
    private String airportFrom;
    private String airportTo;
	private String connectingAirport;
	
	public Route() {

	}
	
	public Route(String airportFrom, String airportTo, String connectingAirport) {
		this.airportFrom = airportFrom;
		this.airportTo = airportTo;
		this.connectingAirport = connectingAirport;
	}
	
	public String getAirportFrom() {
		return airportFrom;
	}
	public String getAirportTo() {
		return airportTo;
	}
	public String getConnectingAirport() {
		return connectingAirport;
	}
	
	public void setAirportFrom(String airportFrom) {
		this.airportFrom = airportFrom;
	}
	public void setAirportTo(String airportTo) {
		this.airportTo = airportTo;
	}
    public void setConnectingAirport(String connectingAirport) {
		this.connectingAirport = connectingAirport;
	}

    @Override
    public String toString() {
        return "Route{" +
                "airportFrom='" + airportFrom + '\'' +
                ", airportTo=" + airportTo +
                ", connectingAirport=" + connectingAirport +
                '}';
    }
    
}
