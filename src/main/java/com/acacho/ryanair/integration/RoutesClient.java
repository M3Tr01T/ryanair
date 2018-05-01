package com.acacho.ryanair.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.acacho.ryanair.model.Route;

@Component
public class RoutesClient {
	@Value("${rest.api.routes.url}")
	private String routesURI;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public Route[] getAllRoutes(){
		try {
			return restTemplate.getForObject(routesURI, Route[].class);
		} catch (Exception e) {
			e.printStackTrace();
			return new Route[0];
		}
	}
	
}
