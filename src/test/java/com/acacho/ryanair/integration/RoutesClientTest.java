package com.acacho.ryanair.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.acacho.ryanair.model.Route;

@RunWith(SpringRunner.class)
@SpringBootTest 
@AutoConfigureMockMvc
public class RoutesClientTest {

    @Autowired
    private RoutesClient routesClient;
    private Route[] routes;
    
    @Before
    public void loadRoutes() {
    	if (routes == null) {
    		routes = routesClient.getAllRoutes();
		}
    }

    @Test
    public void shouldGetRoutes() {
        assertThat(routes.length, greaterThan(0));
    }
    
    @Test
    public void shouldGetRoutesWithCorrectInfo() {
    	for (Route route : routes) {
    		assertThat(route.getAirportFrom(), not(nullValue()));
    		assertThat(route.getAirportFrom().length(), is(3));
    		assertThat(route.getAirportTo(), not(nullValue()));
    		assertThat(route.getAirportTo().length(), is(3));
    		assertThat(route.getAirportFrom(),not(equalTo(route.getAirportTo())));
    	}
    }
    
    @Test
    public void shouldGetDistinctRoutes() {
    	Set<Route> set = new HashSet<>();
    	
    	for (Route route : routes) {
    		assertThat(set.add(route), is(true));
    	}
    }

}
