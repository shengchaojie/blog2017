#!/bin/bash
cd /home/scj/workspace/blog2017
git pull
mvn clean package -Dmaven.test.skip=true
cd blog-web/target
java -jar blog.jar&
