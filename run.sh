#!/bin/bash
pid=`ps -ef|grep blog.jar |grep -v grep |awk '{print $2}'`
if [ -n "$pid" ]
then
echo "pid is $pid"
kill  $pid
echo "killed $pid"
else
echo 'no blog.jar running'
fi
cd /home/scj/workspace/blog2017
git pull
echo 'maven build start'
mvn clean package -Dmaven.test.skip=true>/dev/null
echo 'maven build end'
cd blog-web/target
java -jar blog.jar --spring.profiles.active=prd&
echo 'project running'