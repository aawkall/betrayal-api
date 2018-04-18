FROM tomcat:9.0-jre8

WORKDIR /usr/local/tomcat

COPY build/libs/betrayalapi.war /usr/local/tomcat/webapps
