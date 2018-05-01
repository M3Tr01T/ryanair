package com.acacho.ryanair;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.acacho.ryanair.controller.InterconnectionsControllerTest;
import com.acacho.ryanair.integration.RoutesClientTest;
import com.acacho.ryanair.integration.SchedulesClientTest;
import com.acacho.ryanair.service.InterconnectionServiceTest;
import com.acacho.ryanair.service.LegServiceTest;
import com.acacho.ryanair.service.RoutesServiceTest;
import com.acacho.ryanair.service.ScheduleServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	InterconnectionsControllerTest.class, 
	RoutesClientTest.class, 
	InterconnectionServiceTest.class, 
	LegServiceTest.class,
	RoutesServiceTest.class, 
	ScheduleServiceTest.class, 
	SchedulesClientTest.class 
})
public class RyanairApplicationTests {
	// Empty class
}
