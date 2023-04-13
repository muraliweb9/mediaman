#!/bin/ash

cp /volume1/media/iTunes/iTunes\ Music\ Library.xml config/iTunes\ Music\ Library.xml

java -Xmx512m -cp "lib/*" com.murali.nas.MediaManager -c config/config.properties -i "config/iTunes Music Library.xml"
