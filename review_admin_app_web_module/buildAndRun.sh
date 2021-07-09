#!/bin/sh
mvn clean package && docker build -t org.vbencek/review_admin_app_web_module .
docker rm -f review_admin_app_web_module || true && docker run -d -p 9080:9080 -p 9443:9443 --name review_admin_app_web_module org.vbencek/review_admin_app_web_module