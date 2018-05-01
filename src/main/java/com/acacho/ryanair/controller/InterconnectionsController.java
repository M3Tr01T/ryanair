package com.acacho.ryanair.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acacho.ryanair.model.Interconnection;
import com.acacho.ryanair.services.InterconnectionService;

@RestController
@RequestMapping("/interconnections")
public class InterconnectionsController {
    @Autowired
    private InterconnectionService interconnectionService;
	private static final Logger log = LoggerFactory.getLogger(InterconnectionsController.class);
    
	@RequestMapping
	public List<Interconnection> getConnections(
			@RequestParam(value="departure") String departure, 
			@RequestParam(value="arrival") String arrival,
			@RequestParam(value="departureDateTime") String newDepartureDateTime,
			@RequestParam(value="arrivalDateTime") String newArrivalDateTime
			) {

		try {
			LocalDateTime departureDateTime = LocalDateTime.parse(newDepartureDateTime);
			LocalDateTime arrivalDateTime = LocalDateTime.parse(newArrivalDateTime);
			if (departureDateTime.isAfter(arrivalDateTime)) {
				return new ArrayList<Interconnection>();
			}

			// This field can be dynamically set
			int numStops = 1;
			return interconnectionService.getInterconnections(departure, arrival, departureDateTime, arrivalDateTime, numStops);
			
	    // Catching exceptions to display a more user-friendly output
		} catch (DateTimeParseException  dateTimeParseException ) {
			log.info(dateTimeParseException.toString());
		}
		catch (Exception  e ) {
			log.info(e.toString());
		}

		return new ArrayList<>();
	}

}
