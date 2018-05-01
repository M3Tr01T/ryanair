package com.acacho.ryanair.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.acacho.ryanair.model.Route;
import com.acacho.ryanair.services.RouteService;

@RunWith(SpringRunner.class)
@SpringBootTest 
@AutoConfigureMockMvc
public class RoutesServiceTest {

    @Autowired
    private RouteService routesService;
    
    private List<Route> routes;
    
    @Before
    public void loadRoutes() {
    	if (routes == null) {
    		routes = routesService.getRoutesWithoutConnectingAirport();
		}
    }

    @Test
    public void shouldGetRoutes() throws Exception {
        assertThat(routes, is(not(empty())));
    }
    
    @Test
    public void shouldGetRoutesWithoutConnectingAirports() throws Exception {
    	assertThat(routes.stream().anyMatch(r->r.getConnectingAirport() != null), is(not(true)));
    }
    
    @Test
    public void shouldGetRouteFromWROtoDUB() throws Exception {
    	assertThat(routes.stream().anyMatch(r->r.getAirportFrom().equals("WRO") && r.getAirportTo().equals("DUB")), is(true));
    }
    
    @Test
    public void shouldGetRouteFromWROtoSTN() throws Exception {
    	assertThat(routes.stream().anyMatch(r->r.getAirportFrom().equals("WRO") && r.getAirportTo().equals("STN")), is(true));
    }
    
    @Test
    public void shouldNotGetRouteFromWROtoVGO() throws Exception {
    	assertThat(routes.stream().anyMatch(r->r.getAirportFrom().equals("WRO") && r.getAirportTo().equals("VGO")), is(not(true)));
    }
    
    @Test
    public void shouldNotGetRouteFromVGOtoWRO() throws Exception {
    	assertThat(routes.stream().anyMatch(r->r.getAirportFrom().equals("VGO") && r.getAirportTo().equals("WRO")), is(not(true)));
    }
    
}

