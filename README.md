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
3. The API documentation is generated using spring-restdocs-mockmvc - When you build the app locally, you will have a better idea about the same
4. 

Prerequisites
1. Java Development Kit - version 1.8.x
2. Maven - version 3.2.x or higher
3. Elasticsearch - version 6.4.3
4. Kibana - version 6.4.3
5. IDE : Any IDE of choice - STS preferred for easy integration of Spring features


Installation Steps:
1. Setup Elasticsearch. Follow steps provided on the link below
https://www.elastic.co/downloads/elasticsearch
2. Setup Kibana. Follow steps provided on the link below
https://www.elastic.co/downloads/kibana
3. Create vehicle and reading indices in Elasticsearch using the Mapping provided under Vivek_Dalal_IOT_Lab_project/elasticsearch/mappings
3. Run the following command in the Vivek_Dalal_IOT_Lab_project/cartrackingapp/ 
4. After successful install, run the command mentioned in the  Vivek_Dalal_IOT_Lab_project/cartrackingapp/ run-app-command.txt file

Authors:
Vivek Dalal
