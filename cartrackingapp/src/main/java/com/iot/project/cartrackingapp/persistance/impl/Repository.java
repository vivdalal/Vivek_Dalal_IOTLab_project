package com.iot.project.cartrackingapp.persistance.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.ClearScrollRequest;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchScrollRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.Scroll;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iot.project.cartrackingapp.exception.ServiceException;
import com.iot.project.cartrackingapp.exception.ValidationException;
import com.iot.project.cartrackingapp.model.SensorReading;
import com.iot.project.cartrackingapp.model.Vehicle;
import com.iot.project.cartrackingapp.persistance.connection.ESClient;
import com.iot.project.cartrackingapp.utility.constants.ApplicationConstants;
import com.iot.project.cartrackingapp.utility.constants.enums.AlertLevel;
import com.iot.project.cartrackingapp.validator.DataValidator;

@Component
public class Repository {

	@Autowired
	ObjectMapper mapper;

	@Autowired
	DataValidator dataValidator;

	public void saveVehicle(Vehicle vehicle) {

	}

	public void updateVehicle(Vehicle vehicle) {

	}

	public List<Vehicle> findAllVehicles() throws ServiceException {

		List<Vehicle> vehicleList = null;

		final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
		SearchRequest searchRequest = new SearchRequest(ApplicationConstants.VEHICLE_INDEX);
		searchRequest.types(ApplicationConstants.VEHICLE_TYPE);
		searchRequest.scroll(scroll);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		searchSourceBuilder.size(ApplicationConstants.SCROLL_SIZE);

		searchRequest.source(searchSourceBuilder);


		try {
			SearchResponse searchResponse = ESClient.getConnection().search(searchRequest, RequestOptions.DEFAULT);
			String scrollId = searchResponse.getScrollId();
			SearchHit[] searchHits = searchResponse.getHits().getHits();

			if (searchHits.length > 0) {
				vehicleList = new ArrayList<>();
			}

			for (SearchHit hit : searchHits) {
				vehicleList.add(mapper.readValue(hit.getSourceAsString(), Vehicle.class));
			}

			while (searchHits != null && searchHits.length > 0) {
				SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
				scrollRequest.scroll(scroll);
				searchResponse = ESClient.getConnection().scroll(scrollRequest, RequestOptions.DEFAULT);
				scrollId = searchResponse.getScrollId();
				searchHits = searchResponse.getHits().getHits();
				for (SearchHit hit : searchHits) {
					vehicleList.add(mapper.readValue(hit.getSourceAsString(), Vehicle.class));
				}

			}

			ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
			clearScrollRequest.addScrollId(scrollId);
			ClearScrollResponse clearScrollResponse = ESClient.getConnection().clearScroll(clearScrollRequest,
					RequestOptions.DEFAULT);
			boolean succeeded = clearScrollResponse.isSucceeded();

			if (succeeded) {
				//System.out.println("Scroll context cleared successfully.");
			}

		} catch (IOException ioException) {
			throw new ServiceException("Unable to perform search on the DB", ioException);
		}

		return vehicleList;

	}

	public void deleteVehicleById(String vin) {

	}

	public void bulkSaveVehicles(List<Vehicle> vehicleList) throws ValidationException, ServiceException {
		// Saving the vehicleList to Elasticsearch Index. - Bulk Insertion
		BulkRequest request = new BulkRequest();

		for (Vehicle vehicle : vehicleList) {

			dataValidator.validateVehicle(vehicle);

			try {
				request.add(new IndexRequest(ApplicationConstants.VEHICLE_INDEX, ApplicationConstants.VEHICLE_TYPE,
						vehicle.getVin()).source(mapper.writeValueAsString(vehicle), XContentType.JSON));
			} catch (JsonProcessingException jsonProcessingException) {
				throw new ServiceException("Unable to parse the Vehicle data", jsonProcessingException);
			}

		}

		// Inserting in bulk synchronously
		// BulkResponse bulkResponse = esClient.getConnection().bulk(request,
		// RequestOptions.DEFAULT);
		try {
			ESClient.getConnection().bulk(request, RequestOptions.DEFAULT);
		} catch (IOException ioException) {
			throw new ServiceException("Unable to save the Vehicle data to the DB", ioException);
		}

	}

	public Vehicle findVehicleById(String vin) throws ServiceException {
		Vehicle vehicle = null;

		GetRequest getRequest = new GetRequest(ApplicationConstants.VEHICLE_INDEX, ApplicationConstants.VEHICLE_TYPE,
				vin);

		GetResponse response = null;
		try {
			response = ESClient.getConnection().get(getRequest, RequestOptions.DEFAULT);
			if (response.isExists()) {
				String sourceAsString = response.getSourceAsString();
				vehicle = mapper.readValue(sourceAsString, Vehicle.class);
			}
		} catch (IOException ioException) {
			throw new ServiceException("Something went wrong while fetching the Vehicle data from DB for Vin: " + vin,
					ioException);
		}

		return vehicle;
	}

	public void saveReading(SensorReading sensorReading) throws ServiceException {
		try {
			IndexRequest request = new IndexRequest(ApplicationConstants.READING_INDEX,
					ApplicationConstants.READING_TYPE).source(mapper.writeValueAsString(sensorReading),
							XContentType.JSON);

			ESClient.getConnection().index(request, RequestOptions.DEFAULT);

		} catch (JsonProcessingException jsonProcessingException) {
			throw new ServiceException("Unable to parse the SensorReading", jsonProcessingException);
		} catch (IOException ioException) {
			throw new ServiceException("Unable to save the SensorReading to DB. Vin : " + sensorReading.getVin(),
					ioException);
		}

	}

	public List<SensorReading> findAllHistoricalReadingsForId(String vin) throws ServiceException {

		List<SensorReading> sensorReadingList = null;

		final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));

		SearchRequest searchRequest = new SearchRequest(ApplicationConstants.READING_INDEX);
		searchRequest.types(ApplicationConstants.READING_TYPE);
		searchRequest.scroll(scroll);

		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.termQuery(ApplicationConstants.VIN_FLD, vin));
		searchSourceBuilder.size(ApplicationConstants.SCROLL_SIZE);

		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = ESClient.getConnection().search(searchRequest, RequestOptions.DEFAULT);
			String scrollId = searchResponse.getScrollId();
			SearchHit[] searchHits = searchResponse.getHits().getHits();

			if (searchHits.length > 0) {
				sensorReadingList = new ArrayList<>();
			}

			for (SearchHit hit : searchHits) {
				sensorReadingList.add(mapper.readValue(hit.getSourceAsString(), SensorReading.class));
			}

			while (searchHits != null && searchHits.length > 0) {
				SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
				scrollRequest.scroll(scroll);
				searchResponse = ESClient.getConnection().scroll(scrollRequest, RequestOptions.DEFAULT);
				scrollId = searchResponse.getScrollId();
				searchHits = searchResponse.getHits().getHits();
				for (SearchHit hit : searchHits) {
					sensorReadingList.add(mapper.readValue(hit.getSourceAsString(), SensorReading.class));
				}

			}

			ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
			clearScrollRequest.addScrollId(scrollId);
			ClearScrollResponse clearScrollResponse = ESClient.getConnection().clearScroll(clearScrollRequest,
					RequestOptions.DEFAULT);
			boolean succeeded = clearScrollResponse.isSucceeded();

			if (succeeded) {
				System.out.println("Scroll context cleared successfully.");
			}

		} catch (IOException ioException) {
			throw new ServiceException("Unable to perform search on the DB", ioException);
		}

		return sensorReadingList;

	}

	public List<SensorReading> findHighAlerts(int numOfHours) throws ServiceException {

		List<SensorReading> sensorReadingList = null;

		BoolQueryBuilder alertAndDataRangeQuery = QueryBuilders.boolQuery();
		QueryBuilder dateRangeQuery = QueryBuilders.rangeQuery(ApplicationConstants.TIMESTAMP_FLD)
				.gte("now-" + numOfHours + "h").lte("now");
		QueryBuilder alertStatus = QueryBuilders.termQuery(ApplicationConstants.ALRT_LEVEL_FLD, AlertLevel.HIGH.name());

		alertAndDataRangeQuery.must(alertStatus);
		alertAndDataRangeQuery.must(dateRangeQuery);

		final Scroll scroll = new Scroll(TimeValue.timeValueMinutes(1L));
		SearchRequest searchRequest = new SearchRequest(ApplicationConstants.READING_INDEX);
		searchRequest.types(ApplicationConstants.READING_TYPE);
		searchRequest.scroll(scroll);
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(alertAndDataRangeQuery);
		searchSourceBuilder.size(ApplicationConstants.SCROLL_SIZE);
		searchRequest.source(searchSourceBuilder);

		try {
			SearchResponse searchResponse = ESClient.getConnection().search(searchRequest, RequestOptions.DEFAULT);
			String scrollId = searchResponse.getScrollId();
			SearchHit[] searchHits = searchResponse.getHits().getHits();

			if (searchHits.length > 0) {
				sensorReadingList = new ArrayList<>();
			}

			for (SearchHit hit : searchHits) {
				sensorReadingList.add(mapper.readValue(hit.getSourceAsString(), SensorReading.class));
			}

			while (searchHits != null && searchHits.length > 0) {
				SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);
				scrollRequest.scroll(scroll);
				searchResponse = ESClient.getConnection().scroll(scrollRequest, RequestOptions.DEFAULT);
				scrollId = searchResponse.getScrollId();
				searchHits = searchResponse.getHits().getHits();
				for (SearchHit hit : searchHits) {
					sensorReadingList.add(mapper.readValue(hit.getSourceAsString(), SensorReading.class));
				}

			}
			
			ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
			clearScrollRequest.addScrollId(scrollId);
			ClearScrollResponse clearScrollResponse = ESClient.getConnection().clearScroll(clearScrollRequest,
					RequestOptions.DEFAULT);
			boolean succeeded = clearScrollResponse.isSucceeded();

			if (succeeded) {
				//System.out.println("Scroll context cleared successfully.");
			}

		} catch (IOException ioException) {
			throw new ServiceException("Unable to perform search on the DB", ioException);
		}

		return sensorReadingList;

	}

}
