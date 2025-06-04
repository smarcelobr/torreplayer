#!/bin/bash

echo INICIANDO MUSICA

JAVA_HOME=/usr/lib/jvm/java-17-openjdk-armhf
WORKDIR=/opt/torreplayer
JAVA_OPTIONS=" -Xms512m -Xmx768m -server -Dspring.profiles.active=nave"
# APP_OPTIONS=" -c /path/to/app.config -d /path/to/datadir "
APP_OPTIONS=""

cd $WORKDIR || exit
"${JAVA_HOME}/bin/java" $JAVA_OPTIONS -jar musica.jar $APP_OPTIONS
