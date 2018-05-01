package com.acacho.ryanair.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acacho.ryanair.model.Interconnection;
import com.acacho.ryanair.model.Leg;
import com.acacho.ryanair.model.Route;

@Service
public class InterconnectionService {
	@Autowired
	private RouteService routeService;
	@Autowired
	private LegService legService;

	/**
	 * This functions receives the departure and arrival IATA codes and DateTime besides the number of stops.
	 * Retrieves the routes available between origin and destination from Ryanair.
	 * Then retrieves the suitable legs for the available routes.
	 * 
	 * @param departure IATA code for the departure airport
	 * @param arrival IATA code for the arrival airport
	 * @param departureDateTime LocalDateTime for the departure. No flight should leave before this date and time.
	 * @param arrivalDateTime LocalDateTime for the arrival. No flight should arrive after this date time.
	 * @param numStops maximum number of stops for the route
	 * @return a list of interconnections satisfying the requested logic
	 */
	public List<Interconnection> getInterconnections(String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime, int numStops){
		List<Route> listRoutes = routeService.getRoutesWithoutConnectingAirport();
		List<Route> validListOfRoutes = routeService.createListOfRoutes(listRoutes, departure, arrival, numStops+1);
		List<List<Leg>> listofLists = legService.getListOfListsOfLegs(validListOfRoutes, departure, arrival, departureDateTime, arrivalDateTime, numStops+1);
		ListLegsComparator listLegsComparator = new ListLegsComparator();
		Collections.sort(listofLists, (a, b) -> listLegsComparator.compare(a, b));
		
		List<Interconnection> interconnections = new ArrayList<>();
		for (List<Leg> list : listofLists) {
			Interconnection interconnection = new Interconnection();
			interconnection.setStops(list.size()-1);
			List<Leg> currentLegs = legService.getSuitableLegs(list);
			if (currentLegs.size() == list.size()) {
				interconnection.setLegs(currentLegs);
				interconnections.add(interconnection);
			}
		}

		return interconnections;
	}

}


