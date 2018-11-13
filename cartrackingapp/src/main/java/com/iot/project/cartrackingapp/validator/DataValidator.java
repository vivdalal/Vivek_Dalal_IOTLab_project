package com.iot.project.cartrackingapp.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iot.project.cartrackingapp.exception.ValidationException;
import com.iot.project.cartrackingapp.model.SensorReading;
import com.iot.project.cartrackingapp.model.Vehicle;
import com.iot.project.cartrackingapp.persistance.impl.Repository;

@Component
public class DataValidator {

	@Autowired
	Repository repository;

	// throws Validation Exception when a reading is invalid
	public void validateReading(SensorReading sensorReading) throws ValidationException {
		if (sensorReading == null) {
			throw new ValidationException("Sensor Reading passed is NULL");
		} else if (sensorReading.getVin() == null) {
			throw new ValidationException("Vin for Sensor Reading passed is NULL");
		} else if (sensorReading.getTires() == null) {
			throw new ValidationException("Tires data for Sensor Reading passed is NULL");
		}
		// Can add other validations as per use case in the future

	}

	public void validateVehicle(Vehicle vehicle) throws ValidationException {
		if (vehicle == null) {
			throw new ValidationException("Vehicle passed is NULL");
		} else if (vehicle.getVin() == null) {
			throw new ValidationException("Vin for Vehicle passed is NULL");
		}
		// Can add other validations as per use case in the future

	}

}
