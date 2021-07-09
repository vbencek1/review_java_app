@echo off
call mvn clean package
call docker build -t org.vbencek/review_admin_app_web_module .
call docker rm -f review_admin_app_web_module
call docker run -d -p 9080:9080 -p 9443:9443 --name review_admin_app_web_module org.vbencek/review_admin_app_web_module