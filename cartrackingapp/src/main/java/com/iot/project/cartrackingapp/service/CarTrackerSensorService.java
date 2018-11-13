package com.iot.project.cartrackingapp.service;

import java.util.List;

import com.iot.project.cartrackingapp.exception.DataSyncException;
import com.iot.project.cartrackingapp.exception.NoDataFoundException;
import com.iot.project.cartrackingapp.exception.ServiceException;
import com.iot.project.cartrackingapp.exception.ValidationException;
import com.iot.project.cartrackingapp.model.SensorReading;
import com.iot.project.cartrackingapp.model.Vehicle;

public interface CarTrackerSensorService {

	// Input: None
	// Output: List all the vehicles whose data has been reported till date
	public List<Vehicle> findAllVehicles() throws ServiceException, NoDataFoundException;

	// Input: Number of hours from current time for which the query needs to
	// performed
	// Output: All High Alert Readings for the past numOfHours
	public List<SensorReading> findHighAlerts(int numOfHours) throws NoDataFoundException, ServiceException;

	// Input: VIN for a paerticular vehicle
	// Output: List of all the SensorReading recorded for the particular Vehicle
	// with VIN = vin
	public List<SensorReading> findAllHistoricalReadings(String vin) throws ServiceException, NoDataFoundException;

	public void saveAllVehicles(List<Vehicle> vehicleList) throws ValidationException, ServiceException;

	public SensorReading saveReading(SensorReading sensorReading)
			throws DataSyncException, ServiceException, ValidationException;

}
