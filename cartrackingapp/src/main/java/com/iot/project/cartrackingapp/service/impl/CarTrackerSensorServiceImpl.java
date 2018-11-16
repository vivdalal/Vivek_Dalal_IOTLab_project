package com.iot.project.cartrackingapp.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iot.project.cartrackingapp.alert.rules.AlertRules;
import com.iot.project.cartrackingapp.exception.DataSyncException;
import com.iot.project.cartrackingapp.exception.NoDataFoundException;
import com.iot.project.cartrackingapp.exception.ServiceException;
import com.iot.project.cartrackingapp.exception.ValidationException;
import com.iot.project.cartrackingapp.model.SensorReading;
import com.iot.project.cartrackingapp.model.Vehicle;
import com.iot.project.cartrackingapp.model.alert.AlertWrapper;
import com.iot.project.cartrackingapp.persistance.impl.Repository;
import com.iot.project.cartrackingapp.service.CarTrackerSensorService;
import com.iot.project.cartrackingapp.validator.DataValidator;

@Service
public class CarTrackerSensorServiceImpl implements CarTrackerSensorService {

	private static final Logger logger = LogManager.getLogger(CarTrackerSensorServiceImpl.class);

	@Autowired
	Repository repository;

	@Autowired
	DataValidator dataValidator;

	@Autowired
	AlertRules alertRules;

	@Override
	public List<Vehicle> findAllVehicles() throws ServiceException, NoDataFoundException {

		logger.debug("Find All Vehicles Service called.");
		// Getting all the readings for the Vin passed as input
		List<Vehicle> vehicleList = repository.findAllVehicles();
		if (vehicleList == null) {
			// No readings found for the vin passed
			logger.error("No Vehicle Data found.");
			throw new NoDataFoundException("No vehicle data found.");
		}
		logger.debug("Returning the Vehicle list retrieved from Elasticsearch");
		return vehicleList;
	}

	@Override
	public List<SensorReading> findHighAlerts(int numOfHours) throws NoDataFoundException, ServiceException {
		List<SensorReading> sensorReadingList = repository.findHighAlerts(numOfHours);

		if (sensorReadingList == null) {
			// No readings found for the vin passed
			logger.error("No Sensor Reading data found for Vehicle in the last " + numOfHours + ".");
			throw new NoDataFoundException("No Sensor Reading data found for Vehicle in the last " + numOfHours + ".");
		}

		logger.debug("Returning the sensor list retrieved from Elasticsearch");
		return sensorReadingList;
	}

	@Override
	public List<SensorReading> findAllHistoricalReadings(String vin) throws ServiceException, NoDataFoundException {
		// Getting all the readings for the Vin passed as input
		List<SensorReading> sensorReadingList = repository.findAllHistoricalReadingsForId(vin);
		if (sensorReadingList == null) {
			// No readings found for the vin passed
			logger.error("No reading found for VIN : " + vin);
			throw new NoDataFoundException("No reading found for VIN : " + vin);
		}

		logger.debug("Returning the sensor list retrieved from Elasticsearch");
		return sensorReadingList;
	}

	@Override
	public void saveAllVehicles(List<Vehicle> vehicleList) throws ValidationException, ServiceException {
		// Saving all the vehicles to Elasticsearch
		logger.debug("Saving all Vehicle data in Bulk fashion");
		repository.bulkSaveVehicles(vehicleList);
		logger.debug("Successfully persisted vehicle data in Elasticsearch");

	}

	@Override
	public void saveReading(SensorReading sensorReading)
			throws DataSyncException, ServiceException, ValidationException {

		// Saving the reading after running validation and business rules specified

		// Vehicle data for this reading is present in the DB
		// Validating the Reading data
		logger.debug("Validating the Sensor request");
		dataValidator.validateReading(sensorReading);

		// Checking whether the vehicle data is present for the reading received
		Vehicle vehicle = repository.findVehicleById(sensorReading.getVin());
		if (vehicle == null) {
			// No vehicle data available for the reading recieved.
			logger.error("Vehicle information for this sensor reading is unavailable");
			throw new DataSyncException("Vehicle information for this sensor reading is unavailable");
		}

		// If not exception, we will now run the business rules and determine the alert
		// status
		AlertWrapper alertWrapper = alertRules.checkForAlerts(sensorReading, vehicle);

		if (alertWrapper == null || alertWrapper.getAlertLevel() == null) {
			logger.error("Validation of reading has failed for Vin" + sensorReading.getVin());
			throw new ServiceException("Validation of reading has failed for Vin" + sensorReading.getVin());
		}

		// Validation completed successfully.
		// Updating the SensorReading with the alert details
		logger.debug("Setting the alert information into the sensor reading before storing it to Elasticsearch.");
		sensorReading.setAlertLevel(alertWrapper.getAlertLevel());
		sensorReading.setAlertReason(alertWrapper.getAlertReason());

		// Saving the Reading to DB
		repository.saveReading(sensorReading);

		logger.debug("Successfully persisted the sensor reading in Elasticsearch");

	}

	@Override
	public void createIndices() throws ServiceException, DataSyncException {
		logger.info("Request to create indices in Elasticsearch received");
		repository.createIndices();
		logger.info("Request to create indices in Elasticsearch processed");

	}

	@Override
	public void deleteIndices() throws ServiceException, DataSyncException {
		logger.info("Request to delete indices in Elasticsearch received");
		repository.deleteIndices();
		logger.info("Request to delete indices in Elasticsearch processed");

	}

}
