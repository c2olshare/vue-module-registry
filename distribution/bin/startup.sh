#!/bin/bash

if [ ! -f "${JAVA_HOME}/bin/java" ];then
  echo "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"
  exit 1
fi

export JAVA="$JAVA_HOME/bin/java"
export BASE_DIR=`cd $(dirname $0)/..; pwd`
export DEFAULT_CONFIG_LOCATIONS="classpath:/,classpath:/config/,file:./,file:./config/"
export CUSTOM_CONFIG_LOCATIONS=${DEFAULT_CONFIG_LOCATIONS},file:${BASE_DIR}/conf/

JAVA_OPT="${JAVA_OPT} -Xms512m -Xmx512m -Xmn256m"
JAVA_OPT="${JAVA_OPT} -Dvmr.home=${BASE_DIR}"
JAVA_OPT="${JAVA_OPT} -jar ${BASE_DIR}/boot/vmr-web.jar"
JAVA_OPT="${JAVA_OPT} --spring.config.location=${CUSTOM_CONFIG_LOCATIONS}"

echo "$JAVA ${JAVA_OPT}"
nohup $JAVA ${JAVA_OPT} vmr.web >> /dev/null 2>&1 &
echo "vmr is starting，you can check the ${BASE_DIR}/logs/vmr.log"
