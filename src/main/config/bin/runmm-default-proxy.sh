#!/bin/bash

cp /volume1/media/iTunes/iTunes\ Music\ Library.xml config/iTunes\ Music\ Library.xml

java -Xmx512m -Dhttp.proxyHost=surf-proxy.intranet.db.com -Dhttp.proxyPort=8080 -cp "lib/*" com.murali.nas.MediaManager -c config/configNoOverride.properties -r "config/iTunes Music Library.xml"
