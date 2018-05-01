package com.acacho.ryanair.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.acacho.ryanair.model.Leg;
import com.acacho.ryanair.model.Route;
import com.acacho.ryanair.services.LegService;

@RunWith(SpringRunner.class)
@SpringBootTest 
@AutoConfigureMockMvc
public class LegServiceTest {

    @Autowired
    private LegService legService;
    
    private String DUB = "DUB";
    private String WRO = "WRO";
    private String RYG = "RYG";

    @Test
    public void shouldGetListOfListsOfLegsForSingleLegRoute() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-01T22:00");
    	List<Route> listRoutes = createListOfRoutes();
    	List<List<Leg>> listOfListsOfLegs = legService.getListOfListsOfLegs(listRoutes, DUB, WRO, departureDateTime, arrivalDateTime, 2);
    	assertThat(listOfListsOfLegs, is(not(empty())));
    	assertThat(listOfListsOfLegs.get(0).get(0).getDepartureAirport().equals(DUB), is(true));
    	assertThat(listOfListsOfLegs.get(0).get(0).getArrivalAirport().equals(WRO), is(true));
    }
    
    @Test
    public void shouldgetListOfListsOfLegsForMultiLegRoute() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
    	List<Route> listRoutes = createListOfRoutes();
    	List<List<Leg>> listOfListsOfLegs = legService.getListOfListsOfLegs(listRoutes, DUB, WRO, departureDateTime, arrivalDateTime, 2);
    	assertThat(listOfListsOfLegs.size() > 0, is(true));
		assertThat(listOfListsOfLegs.get(listOfListsOfLegs.size()-1).get(0).getDepartureAirport().equals(DUB), is(true));
		assertThat(listOfListsOfLegs.get(listOfListsOfLegs.size()-1).get(0).getArrivalAirport().equals(RYG), is(true));
		assertThat(listOfListsOfLegs.get(listOfListsOfLegs.size()-1).get(1).getDepartureAirport().equals(RYG), is(true));
		assertThat(listOfListsOfLegs.get(listOfListsOfLegs.size()-1).get(1).getArrivalAirport().equals(WRO), is(true));
    }
    
    public List<Route> createListOfRoutes() {
    	Route route1 = new Route("DUB", "WRO", null);
    	Route route2 = new Route("DUB", "RYG", null);
    	Route route3 = new Route("RYG", "WRO", null);
    	
    	List<Route> list = new ArrayList<>();
    	list.add(route1);
    	list.add(route2);
    	list.add(route3);
    	return list;
    }

}
