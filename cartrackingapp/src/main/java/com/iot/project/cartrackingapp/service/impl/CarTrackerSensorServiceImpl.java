package com.iot.project.cartrackingapp.service.impl;

import java.util.List;

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
import com.iot.project.cartrackingapp.utility.constants.enums.AlertLevel;
import com.iot.project.cartrackingapp.validator.DataValidator;

@Service
public class CarTrackerSensorServiceImpl implements CarTrackerSensorService {

	@Autowired
	Repository repository;

	@Autowired
	DataValidator dataValidator;

	@Autowired
	AlertRules alertRules;

	@Override
	public List<Vehicle> findAllVehicles() throws ServiceException, NoDataFoundException {

		// Getting all the readings for the Vin passed as input
		List<Vehicle> vehicleList = repository.findAllVehicles();
		if (vehicleList == null) {
			// No readings found for the vin passed
			throw new NoDataFoundException("No vehicle data found.");
		}

		return vehicleList;
	}

	@Override
	public List<SensorReading> findHighAlerts(int numOfHours) throws NoDataFoundException, ServiceException {
		List<SensorReading> sensorReadingList = repository.findHighAlerts(numOfHours);

		if (sensorReadingList == null) {
			// No readings found for the vin passed
			throw new NoDataFoundException("No vehicle data found.");
		}

		return sensorReadingList;
	}

	@Override
	public List<SensorReading> findAllHistoricalReadings(String vin) throws ServiceException, NoDataFoundException {
		// Getting all the readings for the Vin passed as input
		List<SensorReading> sensorReadingList = repository.findAllHistoricalReadingsForId(vin);
		if (sensorReadingList == null) {
			// No readings found for the vin passed
			throw new NoDataFoundException("No readings found for VIN : " + vin);
		}

		return sensorReadingList;
	}

	@Override
	public void saveAllVehicles(List<Vehicle> vehicleList) throws ValidationException, ServiceException {
		// Saving all the vehicles to Elasticsearch

		repository.bulkSaveVehicles(vehicleList);

	}

	@Override
	public SensorReading saveReading(SensorReading sensorReading)
			throws DataSyncException, ServiceException, ValidationException {

		// Saving the reading after running validation and business rules specified

		// Vehicle data for this reading is present in the DB
		// Validating the Reading data
		dataValidator.validateReading(sensorReading);

		// Checking whether the vehicle data is present for the reading received
		Vehicle vehicle = repository.findVehicleById(sensorReading.getVin());
		if (vehicle == null) {
			// No vehicle data available for the reading recieved.
			throw new DataSyncException("Vehicle information for this sensor reading is unavailable");
		}

		// If not exception, we will now run the business rules and determine the alert
		// status
		AlertWrapper alertWrapper = alertRules.checkForAlerts(sensorReading, vehicle);

		if (alertWrapper == null || alertWrapper.getAlertLevel() == null) {
			throw new ServiceException("Validation of reading has failed for Vin" + sensorReading.getVin());
		}

		// Validation completed successfully.
		// Updating the SensorReading with the alert details
		sensorReading.setAlertLevel(alertWrapper.getAlertLevel());
		sensorReading.setAlertReason(alertWrapper.getAlertReason());

		// Checking whether alert was raised
		if (!alertWrapper.getAlertLevel().equalsIgnoreCase(AlertLevel.NONE.name())) {
			// Alert raised!
			// Save to file?
			// Sending mail with details to recepients
		}

		// Saving the Reading to DB
		repository.saveReading(sensorReading);

		// returning the augmented SensorReading
		return sensorReading;
	}

}
