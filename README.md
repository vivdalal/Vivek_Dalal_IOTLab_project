# iot-project-car-tracker-sensor
Restful Webservice to ingest and analyze IOT data(Car tracker sensor data).
Data is fetched from http://mocker.ennate.academy/
We will use Elasticsearch as our database. It provides a fast, scalable datastore. 
Its JSON based documents provide an extensible design which makes addiing/removing attributes to our dataset easy.
The service provides alerts based on certain rules defined. Eg: If the EngineRPM > RedLineRPM, an alert is raised.
Alerts are shown and analyzed in real time on Kibana dashboard.(Screenshots attached in the documentations/evidences)


Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
1. Clone/Fork this repository : git clone https://github.com/vivdalal/Vivek_Dalal_IOT_Lab_project.git
2. We are using maven as the build tool and have developed the project in Java 1.8. (You will need to install Java 1.8 and Maven 3.2 or higher -  Latest version preferred)

Prerequisites
1. Java Development Kit - version 1.8.x
2. Maven - version 3.2.x or higher
3. Elasticsearch - version 6.4.3
4. Kibana - version 6.4.3
5. IDE : Any IDE of choice
6. Postman or any other Rest Client


Installation Steps:
1. Setup Elasticsearch. Follow steps provided on the link below
https://www.elastic.co/downloads/elasticsearch
2. Setup Kibana. Follow steps provided on the link below
https://www.elastic.co/downloads/kibana
3. Create vehicle and reading indices in Elasticsearch using the Mapping provided under Vivek_Dalal_IOT_Lab_project/elasticsearch/mappings
3. Run the following command in the Vivek_Dalal_IOT_Lab_project/cartrackingapp/ 
4. After successful install, run the command mentioned in the  Vivek_Dalal_IOT_Lab_project/cartrackingapp/ run-app-command.txt file

Setup:
1. Start Elasticsearch on port of choice.
2. Start kibana. Update the Elasticsearch port in the kibana.yml config file
3. Configure the ports for Elasticsearch in the application.properties file of the springboot all

Run the application:
- Once Elasticsearch and Kibana are Up and Running, we can start the springboot app.
- When you start the app for the first time, hit the following endpoint to create the index mapping for vehicle and reading in Elasticsearch
1. http://localhost:8080/cartrackerdata/system/indices/create
- If a HTTP 200 OK response is received, you can now start pushing data using the mocker service
- Go to http://mocker.ennate.academy/
- Put the following endpoint in the Car Tracker Sensor sections
1. http://localhost:8080/cartrackerdata/vehicles
2. http://localhost:8080/cartrackerdata/reading
3. Hit start on both the endpoints to begin pushing data to the service
4. Open Postman



1. Import the IOT-Project.postman_collection.json file into Postman. File can be found in the /documentation/evidences directory
2. Try out the different HTTP POST/PUT/GET/DELETE requests


You can create the visualization of your choice on Kibana. Follow the below steps
  - Spin up Kibana.
  - Go to the URL : http://localhost:5601
  - Go to the Dashboard section from the left
  - Select the attribute from the reading index on which you want live updates. For eg : alertlevel is not NONE
  - The query will execute at the interval you mention and the output will be shown below on the dashboard

Author:
Vivek Dalal
