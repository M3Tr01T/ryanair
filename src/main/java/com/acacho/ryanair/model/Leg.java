package com.acacho.ryanair.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Leg {
	private String departureAirport;
	private String arrivalAirport;
	private LocalDateTime departureDateTime;
	private LocalDateTime arrivalDateTime;
	
	public String getDepartureAirport() {
		return departureAirport;
	}
	public void setDepartureAirport(String departureAirport) {
		this.departureAirport = departureAirport;
	}
	public String getArrivalAirport() {
		return arrivalAirport;
	}
	public void setArrivalAirport(String arrivalAirport) {
		this.arrivalAirport = arrivalAirport;
	}
	public LocalDateTime getDepartureDateTime() {
		return departureDateTime;
	}
	public void setDepartureDateTime(LocalDateTime localDateTime) {
		this.departureDateTime = localDateTime;
	}
	public LocalDateTime getArrivalDateTime() {
		return arrivalDateTime;
	}
	public void setArrivalDateTime(LocalDateTime arrivalDateTime) {
		this.arrivalDateTime = arrivalDateTime;
	}

    @Override
    public String toString() {
        return "Leg{" +
                "departureAirport='" + departureAirport + '\'' +
                ", arrivalAirport=" + arrivalAirport.toString() +
                ", departureDateTime=" + departureDateTime.toString() +
                ", arrivalDateTime=" + arrivalDateTime.toString() +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
    	  if (obj == this) {
              return true;
          }
          if (obj == null || obj.getClass() != this.getClass()) {
              return false;
          }

          Leg leg = (Leg) obj;
    	  return this.getDepartureAirport().equals(leg.getDepartureAirport()) &&
    			  this.getArrivalAirport().equals(leg.getArrivalAirport()) &&
    			  this.getDepartureDateTime().equals(leg.getDepartureDateTime()) &&
    			  this.getArrivalDateTime().equals(leg.getArrivalDateTime());
          
    }
    
    @Override
    public int hashCode() {
    	return this.getDepartureAirport().hashCode() +
    			this.getArrivalAirport().hashCode() +
    			this.getDepartureDateTime().hashCode() +
    			this.getArrivalDateTime().hashCode();
    	
    }
    
}
