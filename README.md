# Ryanair - Travel labs assignment

This solution contains logic for the assignment bellow.
It uses the jGraph library to calculate possible routes and available interconnections of flights. 
It allows to dynamically set the number of maximum stops, and therefore the user can select more than just one stop.

This solution has been developed using Spring Boot.

Several tests are provided, and they all can be run at once from the Test Suite.

## Ryanair - Task 2 - Java/Spring - Interconnecting Flights

Write a Spring MVC based RESTful API application which serves information about possible direct and interconnected flights (maximum 1 stop) based on the data consumed from external APIs.
Given:
The application can consume data from the following two microservices: 

- Routes API:
https://api.ryanair.com/core/3/routes which returns a list of all available routes based on the
airport's IATA codes. Please note that only routes with empty connectingAirport should be used
(value set to null ). 

- Schedules API:
https://api.ryanair.com/timetable/3/schedules/{departure}/{arrival}/years/{year}/months/{month}
which returns a list of available flights for a given departure airport IATA code, an arrival airport
IATA code, a year and a month. 

Requirements:
- The source code of the application should be delivered.
- The application should be assembled as a deployable WAR file
- The application should response to following request URI with given query parameters:

http://<HOST>/<CONTEXT>/interconnections?departure={departure}&arrival={arrival}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime} where:
- departure - a departure airport IATA code
- departureDateTime - a departure datetime in the departure airport timezone in ISO format
- arrival - an arrival airport IATA code
- arrivalDateTime - an arrival datetime in the arrival airport timezone in ISO format

for example:
http://localhost:8080/somevalidcontext/interconnections?departure=DUB&arrival=WRO&departureDateTime=2018-03-01T07:00&arrivalDateTime=2018-03-03T21:00
The application should return a list of flights departing from a given departure airport not earlier than the specified departure datetime and arriving to a given arrival airport not later than the
specified arrival datetime. 

The list should consist of:
- all direct flights if available (for example: DUB - WRO)
- all interconnected flights with a maximum of one stop if available (for example: DUB - STN - WRO)
For interconnected flights the difference between the arrival and the next departure should be 2h or greater

We are looking for the solution to be well factored and to adhere to the SOLID principles.
