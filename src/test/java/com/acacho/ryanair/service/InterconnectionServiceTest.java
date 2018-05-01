package com.acacho.ryanair.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.acacho.ryanair.model.Interconnection;
import com.acacho.ryanair.services.InterconnectionService;


@RunWith(SpringRunner.class)
@SpringBootTest 
@AutoConfigureMockMvc
public class InterconnectionServiceTest{

    @Autowired
    private InterconnectionService interconnectionService;
    
    private String DUB = "DUB";
    private String WRO = "WRO";
    private String VGO = "VGO";

    @Test
    public void shouldFindDirectRoute() throws Exception {
        LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
		LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-02T07:00");
        
        List<Interconnection> interconnections = interconnectionService.getInterconnections(DUB, WRO, departureDateTime, arrivalDateTime, 1);
        
//        assertThat(interconnections, hasSize(1));
        assertThat(interconnections.get(0).getStops(), is(0));
    }

    @Test
    public void shouldFind2LeginterconnectionsIfAny() throws Exception {
        LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
		LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
        
        List<Interconnection> interconnections = interconnectionService.getInterconnections(WRO, DUB, departureDateTime, arrivalDateTime, 1);
        
        assertThat(interconnections.get(0).getStops(), is(0));
        assertThat(interconnections.get(interconnections.size()-1).getStops(), is(1));
    }

    @Test
    public void shouldFind3LeginterconnectionsIfAny() throws Exception {
        LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
		LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-04T07:00");
        
        List<Interconnection> interconnections = interconnectionService.getInterconnections(WRO, DUB, departureDateTime, arrivalDateTime, 2);
        
        assertThat(interconnections.get(0).getStops(), is(0));
        assertThat(interconnections.get(interconnections.size()-1).getStops(), is(2));
    }

    @Test
    public void shouldNotFindDirectinterconnectionsIfNotExist() throws Exception {
        LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
		LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-04T07:00");
        
        List<Interconnection> interconnections = interconnectionService.getInterconnections(WRO, VGO, departureDateTime, arrivalDateTime, 0);
        assertThat(interconnections, empty());
    }

    @Test
    public void shouldNotFindRouteBetweenSameAirports() throws Exception {
        LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
		LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-04T07:00");
        
        assertThat(interconnectionService.getInterconnections(WRO, WRO, departureDateTime, arrivalDateTime,0), empty());
        assertThat(interconnectionService.getInterconnections(DUB, DUB, departureDateTime, arrivalDateTime,1), empty());
    }

    @Test
    public void shouldNotFindRouteForNegativeStops() throws Exception {
        LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
		LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-04T07:00");
        
        assertThat(interconnectionService.getInterconnections(WRO, DUB, departureDateTime, arrivalDateTime,-1), empty());
        assertThat(interconnectionService.getInterconnections(DUB, WRO, departureDateTime, arrivalDateTime,-2), empty());
    }
}
