#!/bin/bash
pid= ps -ef|grep blog-web |grep -v grep |awk '{print $2}'
kill  $pid
echo "killed $pid"
cd /home/scj/workspace/blog2017
git pull
mvn clean package -Dmaven.test.skip=true
cd blog-web/target
java -jar blog.jar&
