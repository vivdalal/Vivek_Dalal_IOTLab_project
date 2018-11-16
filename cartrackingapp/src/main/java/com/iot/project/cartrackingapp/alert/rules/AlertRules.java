package com.iot.project.cartrackingapp.alert.rules;

import org.springframework.stereotype.Component;

import com.iot.project.cartrackingapp.model.SensorReading;
import com.iot.project.cartrackingapp.model.Tires;
import com.iot.project.cartrackingapp.model.Vehicle;
import com.iot.project.cartrackingapp.model.alert.AlertWrapper;
import com.iot.project.cartrackingapp.utility.constants.enums.AlertLevel;

@Component
public class AlertRules {

	public AlertWrapper checkForAlerts(SensorReading sensorReading, Vehicle vehicle) {
		AlertWrapper alert = new AlertWrapper();

		// Checking for High Alert condition. Engine RPM > RedLine RPM
		// If High Alert, then setting the level and reason
		if (sensorReading.getEngineRpm() > vehicle.getRedlineRpm()) {
			// High alert condition met
			alert.setAlertLevel(AlertLevel.HIGH.name());
			alert.setAlertReason(
					"Engine RPM > RedLine PRM by " + (sensorReading.getEngineRpm() - vehicle.getRedlineRpm()));
			return alert;
		} else if (sensorReading.getFuelVolume() < 0.1 * vehicle.getMaxFuelVolume()) {
			// Checking for Medium Alert condition. Fuel Volume < 10% MaxFuelVolume
			// Medium Alert condition is met, setting the level and reason
			alert.setAlertLevel(AlertLevel.MEDIUM.name());
			alert.setAlertReason("Fuel Volume < 10% of Max Fuel Volume");
			return alert;
		} else if (checkTirePressure(sensorReading.getTires())) {
			// Checking for Low Alert condition. Tire pressure of any tire < 32 psi || > 36 psi
			// Low Alert condition is met, setting the level and reason
			alert.setAlertLevel(AlertLevel.LOW.name());
			alert.setAlertReason("Tire Pressure for one of the tires is < 32 psi || > 36 psi");
			return alert;
		}

		// None of the Alert conditions met. SensorReading is normal. No alert to be
		// triggered.
		alert.setAlertLevel(AlertLevel.NONE.name());
		return alert;

	}

	// Input: Tires
	// Output: True if one of the tire pressure is outside the normal range
	private boolean checkTirePressure(Tires tires) {
		if (checkTirePressure(tires.getFrontLeft()) || checkTirePressure(tires.getFrontRight())
				|| checkTirePressure(tires.getRearLeft()) || checkTirePressure(tires.getRearRight())) {
			return true;
		}
		return false;

	}

	// Input : each tire pressure as int
	// Output : true if the tire has pressure < 32 || > 36
	private boolean checkTirePressure(int tire) {
		return tire < 32 || tire > 36 ? true : false;

	}

}
