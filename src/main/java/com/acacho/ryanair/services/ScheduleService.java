package com.acacho.ryanair.services;

import java.time.LocalDateTime;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.acacho.ryanair.integration.SchedulesClient;
import com.acacho.ryanair.model.DayWithFlight;
import com.acacho.ryanair.model.Flight;
import com.acacho.ryanair.model.Schedule;

@Service
public class ScheduleService {
	@Autowired
	private SchedulesClient schedulesApiClient;
	

	/**
	 * Retrieves the schedule from Ryanair passing the information from the parameters.
	 * Filters the information removing the days which are out of our limits. Removes the flights which are not valid as well.
	 * Finally, removes the days which have no flights on them (because they have been filtered out).
	 * @param departure
	 * @param arrival
	 * @param departureDateTime
	 * @param arrivalDateTime
	 * @return
	 */
	@Cacheable("Schedules")
	public Schedule getfilteredSchedule(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
		String monthQuery = String.format("%02d", departureDateTime.getMonthValue());
		String year = departureDateTime.getYear()+"";

		Schedule schedule = schedulesApiClient.getSchedule(departure, arrival, year, monthQuery);
		// An iterator is used to be able to remove the information and avoid the creation of an aux data structure
		for (Iterator<DayWithFlight> iteratorDayWithFlight = schedule.getDaysWithFlight().iterator(); iteratorDayWithFlight.hasNext();) {
			DayWithFlight dayWithFlight = iteratorDayWithFlight.next();
			// If the flight is before or after our limits, it should be not had into account
			if (dayWithFlight.getDay() < departureDateTime.getDayOfMonth() || dayWithFlight.getDay() > arrivalDateTime.getDayOfMonth()) {
				iteratorDayWithFlight.remove();
			} else {
				for (Iterator<Flight> iteratorFlights = dayWithFlight.getFlights().iterator(); iteratorFlights.hasNext(); ) {
					Flight flight = iteratorFlights.next();
					if (!isValid(schedule, dayWithFlight, flight, departureDateTime, arrivalDateTime)) {
						iteratorFlights.remove();
					}

				}
			}

			// If the DayWithFlight has no flights, it means all the flights have been removed
			if (dayWithFlight.getFlights().isEmpty()) {
				iteratorDayWithFlight.remove();
			}
		}

		return schedule;
	}

	/**
	 * Checks if a flight is valid filtering out the ones that are before or after our limit date and times
	 * @param schedule
	 * @param dayWithFlight
	 * @param flight
	 * @param departureDateTime
	 * @param arrivalDateTime
	 * @return true if the flight is valid following the requested logic
	 */
	public boolean isValid(Schedule schedule, DayWithFlight dayWithFlight, Flight flight, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
		String year = departureDateTime.getYear()+"";
		String month = String.format("%02d", schedule.getMonth()); 
		String day = String.format("%02d", dayWithFlight.getDay()); 
		LocalDateTime depDateTime = LocalDateTime.parse(year +"-"+ month +"-"+ day +"T"+ flight.getDepartureTime());
		LocalDateTime arrDateTime = LocalDateTime.parse(year +"-"+ month +"-"+ day +"T"+ flight.getArrivalTime());

		return depDateTime.isAfter(departureDateTime) && arrDateTime.isBefore(arrivalDateTime);
	}

}
