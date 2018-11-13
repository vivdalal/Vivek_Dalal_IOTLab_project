package com.iot.project.cartrackingapp.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.iot.project.cartrackingapp.exception.DataSyncException;
import com.iot.project.cartrackingapp.exception.NoDataFoundException;
import com.iot.project.cartrackingapp.exception.ServiceException;
import com.iot.project.cartrackingapp.exception.ValidationException;
import com.iot.project.cartrackingapp.model.SensorReading;
import com.iot.project.cartrackingapp.model.Vehicle;
import com.iot.project.cartrackingapp.service.CarTrackerSensorService;

@RestController
@EnableAutoConfiguration
@ComponentScan(value = "com.iot.project")
@RequestMapping(value = "/cartrackerdata")
public class CarTrackerSensorController {

	@Autowired
	CarTrackerSensorService carTrackerSensorService;

	// For testing purpose
	@GetMapping("/")
	public Map<String, Object> greeting() {
		return Collections.singletonMap("message", "Hello World");
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/vehicles", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void putVehicles(@RequestBody List<Vehicle> vehicleList) throws ValidationException, ServiceException {
		carTrackerSensorService.saveAllVehicles(vehicleList);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/reading", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void saveReading(@RequestBody SensorReading sensorReading)
			throws DataSyncException, ServiceException, ValidationException {
		carTrackerSensorService.saveReading(sensorReading);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/vehicles", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<Vehicle> findAllVehicles() throws ServiceException, NoDataFoundException {
		return carTrackerSensorService.findAllVehicles();

	}

	@RequestMapping(method = RequestMethod.GET, value = "/highalerts/{numOfHours}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<SensorReading> findHighAlerts(@PathVariable("numOfHours") int numOfHours)
			throws NoDataFoundException, ServiceException {
		return carTrackerSensorService.findHighAlerts(numOfHours);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/histreading/{vin}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<SensorReading> findAllHistoricalReadingsForId(@PathVariable("vin") String vin)
			throws ServiceException, NoDataFoundException {
		return carTrackerSensorService.findAllHistoricalReadings(vin);
	}

}
