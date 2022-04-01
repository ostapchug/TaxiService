package com.example.taxiservice.web.command.trip;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.taxiservice.dao.DBManager;
import com.example.taxiservice.dao.mysql.MySqlCarDao;
import com.example.taxiservice.dao.mysql.MySqlLocationDao;
import com.example.taxiservice.dao.mysql.MySqlPersonDao;
import com.example.taxiservice.dao.mysql.MySqlTripDao;
import com.example.taxiservice.model.Car;
import com.example.taxiservice.model.Role;
import com.example.taxiservice.model.Trip;
import com.example.taxiservice.model.TripStatus;
import com.example.taxiservice.model.dto.TripDto;
import com.example.taxiservice.service.CarService;
import com.example.taxiservice.service.LocationService;
import com.example.taxiservice.service.PersonService;
import com.example.taxiservice.service.TripService;
import com.example.taxiservice.web.Page;
import com.example.taxiservice.web.Path;
import com.example.taxiservice.web.command.Command;

public class TripPageCommand extends Command {

	private static final long serialVersionUID = -4793599624749188289L;
	private static final Logger LOG = LoggerFactory.getLogger(TripPageCommand.class);

	@Override
	public Page execute(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		LOG.debug("Command start");
		
		Page result = null;
		HttpSession session = request.getSession(false);
		String role = (String) session.getAttribute("personRole");
		String lang = (String) session.getAttribute("locale");
		long personId = (long) session.getAttribute("personId");
		long tripId;
		Trip trip = null;
		String errorMessage = null;
		TripService tripService = new TripService(new MySqlTripDao(DBManager.getDataSource()));
		
		String id = request.getParameter("trip");
		
		try {
			tripId = Long.parseLong(id);
			trip = tripService.find(tripId);
		}catch(NumberFormatException e) {
			errorMessage = "trip_not_found";	
		}
		
		if(errorMessage == null && trip != null) {
			CarService carService = new CarService(new MySqlCarDao(DBManager.getDataSource()));
			List<Car> cars = carService.findCarsByTripId(trip.getId());
			
			if(Role.ADMIN.getName().equals(role)) {
				TripDto tripDto = getTripDto(trip, cars, lang);
				request.setAttribute("trip", tripDto);
				result = new Page(Path.PAGE__TRIP);
			}else {
				
				if(trip.getPersonId().equals(personId)) {
					TripDto tripDto = getTripDto(trip, cars, lang);
					request.setAttribute("trip", tripDto);
					result = new Page(Path.PAGE__TRIP);
				}else {
					result = new Page(Path.COMMAND__TRIPS_PAGE, true);
				}
			}
			
		}else {
			result = new Page(Path.COMMAND__TRIPS_PAGE, true);
		}
		
		LOG.debug("Command finish");
		return result;
	}
	
	private TripDto getTripDto(Trip trip, List<Car> cars, String lang) {
		TripDto result = new TripDto();
		LocationService locationService = new LocationService(new MySqlLocationDao(DBManager.getDataSource()));
		PersonService personService = new PersonService(new MySqlPersonDao(DBManager.getDataSource()));
		
		String origin = locationService.find(trip.getOriginId(), lang).toString();
		String destination = locationService.find(trip.getDestinationId(), lang).toString();
		String phone = personService.find(trip.getPersonId()).getPhone();
		TripStatus tripStatus = TripStatus.getTripStatus(trip);
		
		result.setId(trip.getId());
		result.setPersonPhone(phone);
		result.setOrigin(origin);
		result.setDestination(destination);
		result.setDistance(trip.getDistance());
		result.setDate(trip.getDate());
		result.setBill(trip.getBill());
		result.setStatus(tripStatus.getName());
		result.setCars(cars);
		
		return result;
	}

}
