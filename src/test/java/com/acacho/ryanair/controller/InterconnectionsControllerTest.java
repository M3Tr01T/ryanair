package com.acacho.ryanair.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest 
@AutoConfigureMockMvc
public class InterconnectionsControllerTest  {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterconnectionsControllerTest.class);

    private static final String INTERCONNECTIONS_URI = "/interconnections?departure={departure}&arrival={arrival}&" +
            "departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}";

	@Autowired
    private MockMvc mvc;

    @Test
    public void shouldGetSingleLegFlights() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-02T07:00");
        MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "DUB", "WRO", departureDateTime, arrivalDateTime);
        
        ResultActions perform = mvc.perform(requestBuilder);
        perform.andExpect(status().isOk())
            .andDo(result -> LOGGER.info(result.getResponse().getContentAsString()))
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$[0].stops", is(0)))
            .andExpect(jsonPath("$[0].legs.length()", is(1)))
            .andExpect(jsonPath("$[0].legs[0].departureAirport", is("DUB")))
            .andExpect(jsonPath("$[0].legs[0].arrivalAirport", is("WRO")))
            .andExpect(jsonPath("$[0].legs[0].departureDateTime", is("2018-08-01T17:50:00")))
            .andExpect(jsonPath("$[0].legs[0].arrivalDateTime", is("2018-08-01T21:25:00")))
        ;
    }
    
    @Test
    public void shouldGetMultiLegFlights() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
    	MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "DUB", "WRO", departureDateTime, arrivalDateTime);
    	
    	ResultActions perform = mvc.perform(requestBuilder);
    	perform.andExpect(status().isOk())
    	.andDo(result -> LOGGER.info(result.getResponse().getContentAsString()))
    	.andExpect(content().contentType("application/json;charset=UTF-8"))
    	.andExpect(jsonPath("$[2].stops", is(1)))
    	.andExpect(jsonPath("$[2].legs.length()", is(2)))
    	.andExpect(jsonPath("$[2].legs[0].departureAirport", is("DUB")))
    	.andExpect(jsonPath("$[2].legs[1].arrivalAirport", is("WRO")))
    	;
    }

    @Test
    public void shouldGetEmptyFlightsWhenDepartureEqualsArrival() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
        MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "WRO", "WRO", departureDateTime, arrivalDateTime);
        
        ResultActions perform = mvc.perform(requestBuilder);
        perform.andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(content().string("[]"))
        ;
    }

    @Test
    public void shouldGetEmptyFlightsWhenDepartureAfterArrival() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
        MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "DUB", "WRO", arrivalDateTime, departureDateTime);
        
        ResultActions perform = mvc.perform(requestBuilder);
        perform.andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(content().string("[]"))
        ;
    }
    
    @Test
    public void shouldGetEmptyFlightsWhenNullDeparture() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
    	MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, null, "WRO", arrivalDateTime, departureDateTime);
    	
    	ResultActions perform = mvc.perform(requestBuilder);
    	perform.andExpect(status().isOk())
    	.andExpect(content().contentType("application/json;charset=UTF-8"))
    	.andExpect(content().string("[]"))
    	;
    }
    
    @Test
    public void shouldGetEmptyFlightsWhenEmptyDeparture() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
    	MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "", "WRO", arrivalDateTime, departureDateTime);
    	
    	ResultActions perform = mvc.perform(requestBuilder);
    	perform.andExpect(status().isOk())
    	.andExpect(content().contentType("application/json;charset=UTF-8"))
    	.andExpect(content().string("[]"))
    	;
    }
    
    @Test
    public void shouldGetEmptyFlightsWhenEmptyArrival() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
    	MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "DUB", "", arrivalDateTime, departureDateTime);
    	
    	ResultActions perform = mvc.perform(requestBuilder);
    	perform.andExpect(status().isOk())
    	.andExpect(content().contentType("application/json;charset=UTF-8"))
    	.andExpect(content().string("[]"))
    	;
    }
    
    @Test
    public void shouldGetEmptyFlightsWhenNullArrival() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
    	MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "DUB", null, arrivalDateTime, departureDateTime);
    	
    	ResultActions perform = mvc.perform(requestBuilder);
    	perform.andExpect(status().isOk())
    	.andExpect(content().contentType("application/json;charset=UTF-8"))
    	.andExpect(content().string("[]"))
    	;
    }
    
    @Test
    public void shouldGetEmptyFlightsWhenNullDepartureDateTime() throws Exception {
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
    	MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "DUB", "WRO", null, arrivalDateTime);
    	
    	ResultActions perform = mvc.perform(requestBuilder);
    	perform.andExpect(status().isOk())
    	.andExpect(content().contentType("application/json;charset=UTF-8"))
    	.andExpect(content().string("[]"))
    	;
    }
    
    @Test
    public void shouldGetEmptyFlightsWhenNullArrivalDateTime() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "DUB", "WRO", departureDateTime, null);
    	
    	ResultActions perform = mvc.perform(requestBuilder);
    	perform.andExpect(status().isOk())
    	.andExpect(content().contentType("application/json;charset=UTF-8"))
    	.andExpect(content().string("[]"))
    	;
    }
    
    @Test
    public void shouldGetEmptyFlightsWhenMultiplesNullValues() throws Exception {
    	MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, null, null, null, null);
    	
    	ResultActions perform = mvc.perform(requestBuilder);
    	perform.andExpect(status().isOk())
    	.andExpect(content().contentType("application/json;charset=UTF-8"))
    	.andExpect(content().string("[]"))
    	;
    }
    
    @Test
    public void shouldGetEmptyFlightsWhenMultiplesEmptyAndNullValues() throws Exception {
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-03T07:00");
    	MockHttpServletRequestBuilder requestBuilder = get(INTERCONNECTIONS_URI, "", "", null, arrivalDateTime);
    	
    	ResultActions perform = mvc.perform(requestBuilder);
    	perform.andExpect(status().isOk())
    	.andExpect(content().contentType("application/json;charset=UTF-8"))
    	.andExpect(content().string("[]"))
    	;
    }
    
}
