FROM jboss/wildfly:10.0.0.Final

ADD target/javaee-websocket.war ${JBOSS_HOME}/standalone/deployments/javaee-websocket.war

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]