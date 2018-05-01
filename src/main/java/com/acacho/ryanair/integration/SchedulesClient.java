package com.acacho.ryanair.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.acacho.ryanair.model.Schedule;

@Component
public class SchedulesClient {
	@Value("${rest.api.schedules.url}")
	private String schedulesURI;
	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * 
	 * @param departure IATA code for the departure airport
	 * @param arrival IATA code for the arrival airport
	 * @param year 
	 * @param month
	 * @return
	 */
	public Schedule getSchedule(String departure, String arrival, String year, String month){
		try {
			Schedule schedule = restTemplate.getForObject(
					 schedulesURI, 
					 Schedule.class, departure, arrival, year, month);
			return schedule;
		} catch (Exception e) {
			e.printStackTrace();
			return new Schedule();
		}
		
	}

}
