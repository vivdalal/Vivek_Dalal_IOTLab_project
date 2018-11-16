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
	// Action : Fetches all the vehicles from Elasticsearch
	public List<Vehicle> findAllVehicles() throws ServiceException, NoDataFoundException;

	// Input: Number of hours from current time for which the query needs to
	// performed
	// Output: All High Alert Readings for the past numOfHours
	// Action : Fetches all the High Alerts in the past numOfHours(2 for our case)
	// from Elasticsearch
	public List<SensorReading> findHighAlerts(int numOfHours) throws NoDataFoundException, ServiceException;

	// Input: VIN for a paerticular vehicle
	// Output: List of all the SensorReading recorded for the particular Vehicle
	// with VIN = vin
	// Action : Fetches all historical reading in a paginated way from Elasticsearch
	// for the vin
	public List<SensorReading> findAllHistoricalReadings(String vin) throws ServiceException, NoDataFoundException;

	// Input : List of Vehicles
	// Output : None
	// Action : Save the vehicle list to DB - Elasticsearch
	public void saveAllVehicles(List<Vehicle> vehicleList) throws ValidationException, ServiceException;

	// Input : Sensor Data
	// Output : None
	// Action : 1. Validate the sensor data
	// 2. Check whether the Vehicle for which this data is reported is present in Elasticsearch
	// 3. Run Alert Business rules and save the results in the Reading document in Elasticsearch
	// 4. Save the sensor data to DB - Elasticsearch
	public void saveReading(SensorReading sensorReading)
			throws DataSyncException, ServiceException, ValidationException;
	
	// Input : None
	// Output : None
	// Action : Create the indices with the defined mapping and settings in Elasticsearch
	public void createIndices() throws ServiceException, DataSyncException;
	
	
	// Input : None
	// Output : None
	// Action : Delete the indices with the defined mapping and settings in Elasticsearch	
	public void deleteIndices() throws ServiceException, DataSyncException;

}
