package com.iot.project.cartrackingapp.persistance.connection;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.iot.project.cartrackingapp.utility.constants.ApplicationConstants;

//Wrapper class to the RestHighLevelClient
@Component
public class ESClient {
	

	private static String HOST;
	private static int PORT_ONE;
	private static int PORT_TWO;
	private static final String SCHEME = ApplicationConstants.SCHEME;

	// Singleton instance of the RestHighLevelClient
	private static RestHighLevelClient restHighLevelClient;

	private ESClient() {

	}
	
	
	@Value("${ES.HOST}")
    public void setHost(String host) {
		HOST = host;
    }
	
	@Value("${ES.PORT.ONE}")
    public void setPortOne(String portOne) {
		PORT_ONE = Integer.parseInt(portOne);
    }
	
	@Value("${ES.PORT.TWO}")
    public void setPortTwo(String portTwo) {
		PORT_TWO = Integer.parseInt(portTwo);
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

	public static synchronized void closeConnection() throws IOException {
		restHighLevelClient.close();
		restHighLevelClient = null;
	}
}
