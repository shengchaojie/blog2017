#!/bin/bash
pid=ps -ef|grep blog-web |grep -v grep |awk '{print $2}'
if (($pid >0))
then
echo 'pid is $pid'
kill  $pid
echo "killed $pid"
else
echo 'no blog.jar running'
fi
cd /home/scj/workspace/blog2017
git pull
mvn clean package -Dmaven.test.skip=true
cd blog-web/target
java -jar blog.jar&
