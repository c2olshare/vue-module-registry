#!/bin/bash

cd `dirname $0`/../boot
target_dir=`pwd`

pid=`ps ax | grep -i 'vmr.web' | grep ${target_dir} | grep java | grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
  echo "No vmr.web running."
  exit 1;
fi

echo "The vmr.web(${pid}) is running..."

kill ${pid}

echo "Send shutdown request to vmr.web(${pid}) OK"
