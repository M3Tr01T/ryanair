package com.acacho.ryanair.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.acacho.ryanair.integration.RoutesClient;
import com.acacho.ryanair.model.Route;

@Service
public class RouteService {
	@Autowired
	private RoutesClient routesApiClient;
	
	/**
	 * Retrieves all the routes from Ryanair and filter them to get the ones without connecting airport.
	 * The result is cached to improve efficiency.
	 * 
	 * @return a list of routes with no connecting airports
	 */
	@Cacheable("Route")
	public List<Route> getRoutesWithoutConnectingAirport(){
		Route[] allRoutes = routesApiClient.getAllRoutes();
		return Arrays.stream(allRoutes).filter(route -> route.getConnectingAirport() == null).collect(Collectors.toList());
	}
	
	/**
	 * @param listPaths a list containing all the GraphPaths 
	 * @return a list of lists, each of them containing the list of available routes from origin to destination
	 */
	public List<List<Route>> createListOfListsFromGraph(List<GraphPath<String, RouteEdge>> listPaths) {
		List<List<Route>> listRoutes = new ArrayList<>();
		for (GraphPath<String, RouteEdge> graphPath : listPaths) {
				List<Route> newList = new ArrayList<>();
				newList.addAll(graphPath.getEdgeList().stream().map(rout -> rout.getRoute()).collect(Collectors.toList()));
				listRoutes.add(newList);
		}
		
		return listRoutes;
	}
	
	/**
	 * @param listPaths a list containing all the GraphPaths
	 * @return a list containing all the available routes from origin to destination
	 */
	public List<Route> createListFromGraph(List<GraphPath<String, RouteEdge>> listPaths) {
		List<Route> listRoutes = new ArrayList<>();
		for (GraphPath<String, RouteEdge> graphPath : listPaths) {
			listRoutes.addAll(graphPath.getEdgeList().stream().map(rout -> rout.getRoute()).collect(Collectors.toList()));
		}
		
		return listRoutes;
	}
	
	/**
	 * @param routes a list of routes
	 * @return a DefaultDirectedGraph with RouteEdge edges in which each vertex is an IATA airport code and every edge
	 * 		   an available route between them.
	 */
	public Graph<String, RouteEdge> createGraph(List<Route> routes) {
		Graph<String, RouteEdge> graph = new DefaultDirectedGraph<>(RouteEdge.class);
		for (Route route : routes) {
			graph.addVertex(route.getAirportFrom());
			graph.addVertex(route.getAirportTo());
			graph.addEdge(route.getAirportFrom(), route.getAirportTo(), new RouteEdge(route));
		}
		return graph;
	}
	
	/**
	 * @param graph
	 * @param origin
	 * @param destination
	 * @param numStops
	 * @return a list of paths between the origin and the destination with maximum length of numStops + 1
	 */
	public <T extends DefaultEdge> List<GraphPath<String, T>> computePaths(Graph<String, T> graph, String origin, String destination, int maxLength) {
		try {
			AllDirectedPaths<String, T> paths = new AllDirectedPaths<>(graph);
			return paths.getAllPaths(origin, destination, true, maxLength);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<GraphPath<String, T>>();
		}
	}
	
	/**
	 * It contains several logic: this function creates a graph, compute the paths between origin and destination and returns
	 * a list of lists, each of those lists containing a number of routes.
	 * 
	 * @param listRoutes
	 * @param departure
	 * @param arrival
	 * @param numStops
	 * @return
	 */
	public List<List<Route>> createListOfLists(List<Route> listRoutes, String departure, String arrival, int numStops) {
		Graph<String, RouteEdge> graph = createGraph(listRoutes);
		List<GraphPath<String, RouteEdge>> listGraphPaths = computePaths(graph, departure, arrival, numStops);
		return createListOfListsFromGraph(listGraphPaths);
	}
	
	/**
	 * It contains several logic: this function creates a graph, compute the paths between origin and destination and returns
	 * a list containing all the available routes.
	 * 
	 * @param listRoutes
	 * @param departure
	 * @param arrival
	 * @param numStops
	 * @return
	 */
	public List<Route> createListOfRoutes(List<Route> listRoutes, String departure, String arrival, int maxLength) {
		Graph<String, RouteEdge> graph = createGraph(listRoutes);
		List<GraphPath<String, RouteEdge>> listGraphPaths = computePaths(graph, departure, arrival, maxLength);
		return createListFromGraph(listGraphPaths);
	}
}

/**
 * 
 * @author alexc
 *
 * Extension of DefaultEdge which contains a route object
 */
class RouteEdge extends DefaultEdge {
	private static final long serialVersionUID = -2237862916762125215L;
	private Route route;
	
	public RouteEdge(Route route) {
		this.route = route;
	}

	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
	
}

