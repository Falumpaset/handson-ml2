#!/bin/bash
set -e -x

export DEBUG=true

export APP_NAME=importer
export JOB_NAME=importer-job
export SYSTEMD_FILE=/etc/systemd/system/$APP_NAME.service
export RUN_FOLDER=/var/immomio/run
export WORKING_DIRECTORY=/var/immomio/sys
export PACKAGE_FOLDER=/var/immomio/packages/$JOB_NAME

export JAVA_HOME=/usr/lib/jvm/java-8-oracle/jre/bin/
export PATH=$PATH:$JAVA_HOME
export DEFAULT_PROFILE=integration

JAVA_OPTS_VALUE="-Dspring.profiles.active=$DEFAULT_PROFILE"
export JAVA_OPTS_VALUE

truncate -s 0 $PACKAGE_FOLDER/$APP_NAME.conf

echo "JAVA_OPTS=$JAVA_OPTS_VALUE" >> $PACKAGE_FOLDER/$APP_NAME.conf

echo "[Unit]" >> $SYSTEMD_FILE
echo "Description=Immomio Importer" >> $SYSTEMD_FILE
echo "After=syslog.target" >> $SYSTEMD_FILE
echo "" >> $SYSTEMD_FILE
echo "[Service]" >> $SYSTEMD_FILE
echo "User=root" >> $SYSTEMD_FILE
echo "PIDFile=$RUN_FOLDER" >> $SYSTEMD_FILE
echo "WorkingDirectory=$WORKING_DIRECTORY" >> $SYSTEMD_FILE
echo "ExecStart=$PACKAGE_FOLDER.jar" >> $SYSTEMD_FILE
echo "SuccessExitStatus=143" >> $SYSTEMD_FILE
echo "" >> $SYSTEMD_FILE
echo "[Install]" >> $SYSTEMD_FILE
echo "WantedBy=multi-user.target" >> $SYSTEMD_FILE

echo "PATH=$PATH" >> $SYSTEMD_FILE

export EXECUTABLE=$PACKAGE_FOLDER/$APP_NAME.jar

echo

case $1 in

  start)

    mkdir -p $RUN_FOLDER $WORKING_DIRECTORY
    sudo chown -R immomio:immomio $RUN_FOLDER $WORKING_DIRECTORY
    sudo chmod -R 777 $RUN_FOLDER
    sudo chmod -R 777 $WORKING_DIRECTORY
    sudo chmod +x $EXECUTABLE

    cd $RUN_FOLDER

    if [ ! -x /etc/init.d/$APP_NAME ]
      then
        sudo ln -s $EXECUTABLE /etc/init.d/$APP_NAME
    fi

    service $APP_NAME start
    ;;

  stop)
    service $APP_NAME stop

    ;;

  *)
    echo "Usage: ctl {start|stop}" ;;

esac