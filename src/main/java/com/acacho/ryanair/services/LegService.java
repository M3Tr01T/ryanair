package com.acacho.ryanair.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DirectedMultigraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acacho.ryanair.model.DayWithFlight;
import com.acacho.ryanair.model.Flight;
import com.acacho.ryanair.model.Leg;
import com.acacho.ryanair.model.Route;
import com.acacho.ryanair.model.Schedule;

@Service
public class LegService {
	@Autowired
	private ScheduleService scheduleService;
	
	/**
	 * @param validListOfRoutes
	 * @param departure
	 * @param arrival
	 * @param departureDateTime
	 * @param arrivalDateTime
	 * @return
	 */
	public List<List<Leg>> getListOfListsOfLegs(List<Route> validListOfRoutes, String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime, int maxLength) {
		Graph<String, LegEdge> graph = createLegsGraph(validListOfRoutes, departure, arrival, departureDateTime, arrivalDateTime);
		List<GraphPath<String, LegEdge>> listGraphPaths = computePaths(graph, departure, arrival, maxLength);
		return createListOfListsOfLegsFromGraph(listGraphPaths);
	}
	
	/**
	 * This function creates a leg using the passed information. Generates both DepartureDateTime and ArrivalDateTime using
	 * the passed month and day values and the time from the flight parameter.
	 * 
	 * @param route
	 * @param monthValue
	 * @param dayValue
	 * @param departureDateTime
	 * @param arrivalDateTime
	 * @param flight
	 * @return
	 */
	public Leg createLeg(Route route, int monthValue, int dayValue, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime, Flight flight) {
		Leg leg = new Leg();
		leg.setDepartureAirport(route.getAirportFrom());
		leg.setArrivalAirport(route.getAirportTo());
		String month = String.format("%02d", monthValue); 
		String day = String.format("%02d", dayValue);
//		DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.of(departureDateTime.getYear(), monthValue, dayValue, flight.getDepartureTime()));
		leg.setDepartureDateTime(LocalDateTime.parse(departureDateTime.getYear() +"-"+ month +"-"+ day +"T"+ flight.getDepartureTime()));		
		leg.setArrivalDateTime(LocalDateTime.parse(arrivalDateTime.getYear() +"-"+ month +"-"+ day +"T"+ flight.getArrivalTime()));
		return leg;
	}

	/**
	 * @param list of routes used to create the graph
	 * @param departure IATA code for the departure airport
	 * @param arrival IATA code for the final destination airport
	 * @param departureDateTime departure date and time for the beginning of the trip
	 * @param arrivalDateTime arrival date and time for the end of the trip
	 * @return
	 */
	private Graph<String, LegEdge> createLegsGraph(List<Route> list, String departure, String arrival, LocalDateTime departureDateTime, LocalDateTime arrivalDateTime) {
		Graph<String, LegEdge> graph = new DirectedMultigraph<>(LegEdge.class);
		for (Route route : list) {
			Schedule schedule = scheduleService.getfilteredSchedule(departure, arrival, departureDateTime, arrivalDateTime);
			
			for (DayWithFlight dayWithFlight : schedule.getDaysWithFlight()) {
				for (Flight flight : dayWithFlight.getFlights()) {
					Leg leg = createLeg(route, schedule.getMonth(), dayWithFlight.getDay(), departureDateTime, arrivalDateTime, flight);
					graph.addVertex(route.getAirportFrom());
					graph.addVertex(route.getAirportTo());
					graph.addEdge(route.getAirportFrom(), route.getAirportTo(), new LegEdge(leg));
				}
			}
		}
		return graph;
	}
	
	/**
	 * It generates a list of lists, containing each of those lists the legs for each path
	 * @param listPaths
	 * @return
	 */
	public List<List<Leg>> createListOfListsOfLegsFromGraph(List<GraphPath<String, LegEdge>> listPaths) {
		List<List<Leg>> listLegs = new ArrayList<>();
		for (GraphPath<String, LegEdge> graphPath : listPaths) {
			List<Leg> newList = new ArrayList<>();
			newList.addAll(graphPath.getEdgeList().stream().map(leg -> leg.getLeg()).collect(Collectors.toList()));
			listLegs.add(newList);
		}
		
		return listLegs;
	}
	
	/**
	 * @param graph
	 * @param origin
	 * @param destination
	 * @param numStops
	 * @return
	 */
	public <T extends DefaultEdge> List<GraphPath<String, T>> computePaths(Graph<String, T> graph, String origin, String destination, int maxLength) {
		try {
			AllDirectedPaths<String, T> paths = new AllDirectedPaths<>(graph);
			return paths.getAllPaths(origin, destination, true, maxLength);
		} catch (Exception e) {
			e.printStackTrace();
			return new  ArrayList<GraphPath<String, T>>();
		}
		
	}
	
	/**
	 * Returns a list with the legs that satisfied the requested logic (the stop must be of at least 2 hours)
	 * @param list list of all legs retrieved from graph
	 * @return
	 */
	public List<Leg> getSuitableLegs(List<Leg> list) {
		List<Leg> currentLegs = new ArrayList<>();
		for (Leg leg : list) {
			if (currentLegs.size() == 0) {
				currentLegs.add(leg);
			} else if (currentLegs.get(currentLegs.size()-1).getArrivalDateTime().plusHours(2).isAfter(leg.getDepartureDateTime())) {
			  return currentLegs;
			} else {
				currentLegs.add(leg);
			}
		}
		return currentLegs;
	}

}

/**
 * 
 * @author alexc
 *
 * Comparator used to sort the results first by number of stops and second by chronological order.
 */
class ListLegsComparator implements Comparator<List<Leg>>{
	@Override
	public int compare(List<Leg> a, List<Leg> b) {
		if (a.size() != b.size()) {
			return  a.size() < b.size() ? -1 : a.size() > b.size() ? +1 : 0;
		}
		
		int result = 0;
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).getDepartureDateTime().equals(b.get(i).getDepartureDateTime())) {
				continue;
			}
			return a.get(i).getDepartureDateTime().isBefore(b.get(i).getDepartureDateTime()) ? -1 : 1;
		}
		return result;
	}

}

/**
 * 
 * @author alexc
 * 
 * Special edge which contains a leg. It is used to generate a graph with information about the
 * available flights between airports.
 */
class LegEdge extends DefaultEdge {
	private static final long serialVersionUID = -2237862916762125215L;
	private Leg leg;
	
	public LegEdge(Leg leg) {
		this.leg = leg;
	}

	public Leg getLeg() {
		return leg;
	}

	public void setLeg(Leg leg) {
		this.leg = leg;
	}
	
    @Override
    public boolean equals(Object obj) {
    	  if (obj == this) {
              return true;
          }
          if (obj == null || obj.getClass() != this.getClass()) {
              return false;
          }

          LegEdge legEdge = (LegEdge) obj;
    	  return this.getLeg().equals(legEdge.getLeg());
    }
    
    @Override
    public int hashCode() {
    	return leg.hashCode();
    }
	
}
