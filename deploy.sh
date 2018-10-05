#!/bin/bash
 currentDate=$(date +%Y%m%d_%H%M%S)
 beDirOnServer='/home/helpdesk/apps/helpdesk/be'
 server='helpdesk@helpdesk.novahub.vn'
 serverPwd='rzmbE76xsSC'

 sshpass -p $serverPwd ssh $server mkdir ${beDirOnServer}/releases/$currentDate

#  sshpass -p $serverPwd ssh $server rm ${beDirOnServer}/current

 sshpass -p $serverPwd ssh $server ln -s ${beDirOnServer}/releases/$currentDate ${beDirOnServer}/current1

 sshpass -p $serverPwd scp ./target/helpdesk-0.0.1-SNAPSHOT.jar ${server}:/$beDirOnServer/releases/$currentDate/helpdesk.jar

 sshpass -p $serverPwd scp stop.sh start.sh ${server}:/$beDirOnServer

 sshpass -p $serverPwd scp stop.sh start.sh ${server}:/$beDirOnServer

 sshpass -p $serverPwd ssh $server chmod +x ${beDirOnServer}/stop.sh 

 sshpass -p $serverPwd ssh $server chmod +x ${beDirOnServer}/start.sh 

#  sshpass -p $serverPwd ssh $server ${beDirOnServer}/stop.sh 

#  sshpass -p $serverPwd ssh $server $beDirOnServer/start.sh



