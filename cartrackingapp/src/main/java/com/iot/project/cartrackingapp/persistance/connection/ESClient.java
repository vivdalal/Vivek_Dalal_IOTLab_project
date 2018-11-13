package com.iot.project.cartrackingapp.persistance.connection;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.project.cartrackingapp.model.Vehicle;

//Wrapper class to the RestHighLevelClient
@Component
public class ESClient {

	private static final String HOST = "localhost";
	private static final int PORT_ONE = 9200;
	private static final int PORT_TWO = 9201;
	private static final String SCHEME = "http";

	// Singleton instance of the RestHighLevelClient
	private static RestHighLevelClient restHighLevelClient;

	private ESClient() {

	}

	public static RestHighLevelClient getConnection() {

		if (restHighLevelClient == null) {

			synchronized (ESClient.class) {
				if (restHighLevelClient == null) {
					restHighLevelClient = new RestHighLevelClient(RestClient
							.builder(new HttpHost(HOST, PORT_ONE, SCHEME), new HttpHost(HOST, PORT_TWO, SCHEME)));
				}
			}

		}

		return restHighLevelClient;
	}

	private static synchronized void closeConnection() throws IOException {
		restHighLevelClient.close();
		restHighLevelClient = null;
	}

	public static void main(String[] args) throws IOException {
		// Testing the ESClient
		getVehicleById("1234");
		System.out.println("Returned successfully");
		closeConnection();

	}

	private static Vehicle getVehicleById(String id) {
		GetRequest getVehicleRequest = new GetRequest("vehicle", "car", id);
		GetResponse getResponse = null;
		ObjectMapper mapper = new ObjectMapper();
		RestHighLevelClient esClient = getConnection();
		try {
			getResponse = esClient.get(getVehicleRequest, RequestOptions.DEFAULT);
		} catch (java.io.IOException e) {
			e.getLocalizedMessage();
		}
		return getResponse != null ? mapper.convertValue(getResponse.getSourceAsMap(), Vehicle.class) : null;
	}
}
