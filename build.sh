#!/bin/sh
# build|send|run paraflow builders

echo $1" paraflow builders."

PREFIX="dbiir"
HOST="01"
PRESTOHOME="/home/iir/opt/presto-server-0.192"
PARAFLOW_DIR="/home/iir/opt/paraflow-1.0-alpha1"
HADOOPHOME="/home/iir/opt/hadoop-2.7.3"

# build
build()
{
  mvn package
}

# send
send()
{
  echo "scp jars to "$PREFIX$HOST
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-collector-1.0-alpha1.jar d2:/home/iir/opt/paraflow-1.0-alpha1/libs/
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-loader-1.0-alpha1.jar d2:/home/iir/opt/paraflow-1.0-alpha1/libs/
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-test-1.0-alpha1.jar d2:/home/iir/opt/paraflow-1.0-alpha1/libs/
  echo "scp jars to d2"
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-loader-1.0-alpha1.jar d1:/home/iir/opt/paraflow-1.0-alpha1/libs/
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-test-1.0-alpha1.jar d1:/home/iir/opt/paraflow-1.0-alpha1/libs/
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-kafka/target/paraflow-kafka-1.0-alpha1 d1:/home/iir/opt/presto-server-0.192/plugin/
}

# presto
presto()
{
  echo "scp jars to "$PREFIX$HOST
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-kafka/target/paraflow-kafka-1.0-alpha1 d1:/home/iir/opt/presto-server-0.192/plugin/
#  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-kafka3/target/paraflow-kafka3-1.0-alpha1 d1:/home/iir/opt/presto-server-0.192/plugin/
}

# client
client()
{
  echo "scp jars to "$PREFIX$HOST
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-loader-1.0-alpha1.jar d1:/home/iir/opt/paraflow-1.0-alpha1/libs/
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-test-1.0-alpha1.jar d1:/home/iir/opt/paraflow-1.0-alpha1/libs/
  echo "scp jars to d2"
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-collector-1.0-alpha1.jar d2:/home/iir/opt/paraflow-1.0-alpha1/libs/
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-loader-1.0-alpha1.jar d2:/home/iir/opt/paraflow-1.0-alpha1/libs/
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-test-1.0-alpha1.jar d2:/home/iir/opt/paraflow-1.0-alpha1/libs/
  }

# client2
client2()
{
  echo "scp jars to d2"
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-collector-1.0-alpha1.jar d2:/home/iir/opt/paraflow-1.0-alpha1/libs/
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-loader-1.0-alpha1.jar d2:/home/iir/opt/paraflow-1.0-alpha1/libs/
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-test-1.0-alpha1.jar d2:/home/iir/opt/paraflow-1.0-alpha1/libs/
}

# client1
client1()
{
  echo "scp jars to "$PREFIX$HOST
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-loader-1.0-alpha1.jar d1:/home/iir/opt/paraflow-1.0-alpha1/libs/
  scp -r /home/tao/software/station/DBIIR/paraflow/paraflow-1.0-alpha1/libs/paraflow-test-1.0-alpha1.jar d1:/home/iir/opt/paraflow-1.0-alpha1/libs/
}

# run
run()
{
  ssh iir@$PREFIX$HOST "$PRESTOHOME/sbin/stop-all.sh"
  ssh iir@$PREFIX$HOST "$PRESTOHOME/sbin/mv-kafka-plugins.sh"
  ssh iir@$PREFIX$HOST "$PRESTOHOME/sbin/start-all.sh"
}

# delete
delete()
{
  ssh $PREFIX$HOST "$HADOOPHOME/bin/hadoop fs -rm -r /paraflow/test/tpch/*"
}

if [ "$1" = "build" ]; then
  build
elif [ "$1" = "send" ]; then
  send
elif [ "$1" = "client" ]; then
  client
elif [ "$1" = "client2" ]; then
  client2
elif [ "$1" = "client1" ]; then
  client1
elif [ "$1" = "run" ]; then
  run
elif [ "$1" = "delete" ]; then
  delete
elif [ "$1" = "all" ]; then
{
  build
  send
  run
}
elif [ "$1" = "1" ]; then
{
  build
  client
}
elif [ "$1" = "2" ]; then
{
  build
  presto
  run
}
# nothing
else
  echo "Usage: $0 build|send|run|client|all"
fi
