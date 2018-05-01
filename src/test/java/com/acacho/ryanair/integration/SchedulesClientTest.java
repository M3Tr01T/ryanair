package com.acacho.ryanair.integration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.acacho.ryanair.model.Schedule;

@RunWith(SpringRunner.class)
@SpringBootTest 
@AutoConfigureMockMvc
public class SchedulesClientTest {

	@Autowired
	private SchedulesClient schedulesClient;

	@Test
	public void shouldGetFlightsFromDUBToWRO() {
		Schedule schedule = schedulesClient.getSchedule("DUB", "WRO", "2018", "05");
		assertThat(schedule, not(nullValue()));

		assertThat(schedule.getMonth(), is(05));
		assertThat(schedule.getDaysWithFlight(), not(nullValue()));
		assertThat(schedule.getDaysWithFlight().size(), greaterThan(0));

		schedule.getDaysWithFlight().forEach(dayWithFlight -> {
			assertThat(dayWithFlight.getDay(),greaterThan(0));
			assertThat(dayWithFlight.getDay(),lessThanOrEqualTo(31));
			assertThat(dayWithFlight.getFlights(), not(nullValue()));
			assertThat(dayWithFlight.getFlights().size(), greaterThan(0));
			dayWithFlight.getFlights().forEach(flight -> {
				assertThat(flight.getArrivalTime(), not(nullValue()));
				assertThat(flight.getDepartureTime(), not(nullValue()));
			});
		});

	}
	
	@Test
	public void shouldNotGetFlightsWithNullOrigin() {
		Schedule schedule = schedulesClient.getSchedule(null, "WRO", "2018", "05");
		assertThat(schedule.getDaysWithFlight(), nullValue());
	}
	
	@Test
	public void shouldNotGetFlightsWithEmptyOrigin() {
		Schedule schedule = schedulesClient.getSchedule("", "WRO", "2018", "05");
		assertThat(schedule.getDaysWithFlight(), nullValue());
	}
	
	@Test
	public void shouldNotGetFlightsWithEmptyYear() {
		Schedule schedule = schedulesClient.getSchedule("DUB", "WRO", "", "05");
		assertThat(schedule.getDaysWithFlight(), nullValue());
	}
	
	@Test
	public void shouldNotGetFlightsWithEmptyMonth() {
		Schedule schedule = schedulesClient.getSchedule("DUB", "WRO", "2018", "");
		assertThat(schedule.getDaysWithFlight(), nullValue());
	}
	
	@Test
	public void shouldNotGetFlightsWithNullYear() {
		Schedule schedule = schedulesClient.getSchedule("DUB", "WRO", null, "05");
		assertThat(schedule.getDaysWithFlight(), nullValue());
	}
	
	@Test
	public void shouldNotGetFlightsWithNullMonth() {
		Schedule schedule = schedulesClient.getSchedule("DUB", "WRO", "2018", null);
		assertThat(schedule.getDaysWithFlight(), nullValue());
	}
	
	@Test
	public void shouldNotGetFlightsWithAllFieldsNull() {
		Schedule schedule = schedulesClient.getSchedule(null, null, null, null);
		assertThat(schedule.getDaysWithFlight(), nullValue());
	}
	
	@Test
	public void shouldNotGetFlightsWithAllFieldsEmpty() {
		Schedule schedule = schedulesClient.getSchedule("", "", "", "");
		assertThat(schedule.getDaysWithFlight(), nullValue());
	}
	
	@Test
	public void shouldNotGetFlightsFromDUBToDUB() {
		Schedule schedule = schedulesClient.getSchedule("DUB", "DUB", "2018", "05");
		assertThat(schedule.getDaysWithFlight(), nullValue());
	}

}
