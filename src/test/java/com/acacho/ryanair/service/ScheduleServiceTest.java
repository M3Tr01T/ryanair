package com.acacho.ryanair.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.acacho.ryanair.model.DayWithFlight;
import com.acacho.ryanair.model.Schedule;
import com.acacho.ryanair.services.ScheduleService;

@RunWith(SpringRunner.class)
@SpringBootTest 
@AutoConfigureMockMvc
public class ScheduleServiceTest{

    @Autowired
    private ScheduleService scheduleService;

    @Test
    public void shouldGetSchedule() throws Exception {
        LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
		LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-04T07:00");
        Schedule schedule = scheduleService.getfilteredSchedule("DUB", "WRO", departureDateTime, arrivalDateTime);
        
        assertThat(schedule, notNullValue());
    }
    
    @Test
    public void shouldGetScheduleWithDaysWithFlight() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-04T07:00");
    	Schedule schedule = scheduleService.getfilteredSchedule("DUB", "WRO", departureDateTime, arrivalDateTime);
    	
    	assertThat(schedule.getDaysWithFlight(), is(not(empty())));
    }
    
    @Test
    public void shouldGetScheduleWithFlights() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-04T07:00");
    	Schedule schedule = scheduleService.getfilteredSchedule("DUB", "WRO", departureDateTime, arrivalDateTime);
    	
    	for (DayWithFlight dayWithFlight  : schedule.getDaysWithFlight()) {
    		assertThat(dayWithFlight.getFlights(), is(not(empty())));
		}
    }
    
    @Test
    public void shouldGetScheduleWithDepartureBeforeArrival() throws Exception {
    	LocalDateTime departureDateTime = LocalDateTime.parse("2018-08-01T07:00");
    	LocalDateTime arrivalDateTime = LocalDateTime.parse("2018-08-04T07:00");
    	Schedule schedule = scheduleService.getfilteredSchedule("DUB", "WRO", arrivalDateTime, departureDateTime);
    	
    	for (DayWithFlight dayWithFlight  : schedule.getDaysWithFlight()) {
    		assertThat(dayWithFlight.getFlights(), is(not(empty())));
    	}
    }
}
