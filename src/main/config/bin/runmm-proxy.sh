#!/bin/ash

cp /volume1/media/iTunes/iTunes\ Music\ Library.xml config/iTunes\ Music\ Library.xml

java -Dhttp.proxyHost=surf-proxy.intranet.db.com -Dhttp.proxyPort=8080 -cp "lib/*" com.murali.nas.MediaManager -c config/config.properties -i "config/iTunes Music Library.xml"
