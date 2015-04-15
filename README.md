#espmon

Server monitoring system using Esper Engine for monitoring and Kibana for showing the data.

## How to run
### Probe
- follow runProbeInstructions.txt to run the probe

###Client
- follow runClientInstructions.txt to run the client, which is collecting data from probes using JMX

###Elasticsearch
Takes data from logstash and saves them in format which is easily queryable.
- run view/elasticsearch-1.4.2/bin/elasticsearch
This will also deploy Kibana 3 dashboard accessible at http://localhost:9200/_plugin/kibana/index.html#/dashboard/elasticsearch/Total%20dashboard

###Logstash - elasticsearch must be running
Logstash stashes the logs. 
- fill correct path in view/logstash-1.4.2/logstash-esper.conf:3
- run view/logstash-1.4.2/bin/logstash -f logstash-esper.conf

###Kibana 4 beta 3
Experimental use of Kibana 4
- run view/kibana-4.0.0-beta3/bin/kibana
Dashboard will be accessible at http://localhost:5601/ - then click on Dashboard and load "dashboard save" dashboard
(or at http://localhost:5601/#/dashboard/dashboard-save?_g=(time:(from:now-1h,mode:quick,to:now))&_a=(filters:!(),panels:!((col:1,row:1,size_x:6,size_y:2,visId:CPU-LOAD-ON-localhost-port-9998),(col:7,row:1,size_x:6,size_y:2,visId:'Memory-usage-on-localhost:9998'),(col:1,row:3,size_x:6,size_y:2,visId:CPU-LOAD-ON-localhost-port-9999),(col:7,row:3,size_x:6,size_y:2,visId:Memory-usage-on-localhost-port-9999)),query:(query_string:(query:'*')),title:'dashboard%20save'))
