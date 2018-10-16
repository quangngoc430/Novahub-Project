#!/bin/bash

 currentDate=$(date +%Y%m%d_%H%M%S)
 beDirOnServer='/home/helpdesk/apps/helpdesk/be'
 server='helpdesk@helpdesk.novahub.vn'
 jarFile='helpdesk-0.0.1-SNAPSHOT.jar'

#  Make new directory to store new jar file
 ssh $server mkdir ${beDirOnServer}/releases/$currentDate

# Upload jar file to new directory
 scp ./target/$jarFile ${server}:/$beDirOnServer/releases/$currentDate/helpdesk.jar

# Copy script to start and stop app to server
 scp stop.sh start.sh ${server}:/$beDirOnServer

 ssh $server chmod +x ${beDirOnServer}/stop.sh ${beDirOnServer}/start.sh

#  Stop app temporarily
 ssh $server ${beDirOnServer}/stop.sh 

# Remove 'current' symbolic link
 ssh $server rm ${beDirOnServer}/current

# Make new symbolic link to new directory
 ssh $server ln -s ${beDirOnServer}/releases/$currentDate ${beDirOnServer}/current

# Start application helpdesk
 ssh $server $beDirOnServer/start.sh