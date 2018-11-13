package com.iot.project.cartrackingapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code=HttpStatus.BAD_REQUEST)
public class DataSyncException extends Exception {

	public DataSyncException() {
		super();

	}

	public DataSyncException(String message, Throwable cause) {
		super(message, cause);

	}

	public DataSyncException(String message) {
		super(message);

	}

	public DataSyncException(Throwable cause) {
		super(cause);
	}

}
