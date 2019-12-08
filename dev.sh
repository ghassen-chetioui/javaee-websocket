export M2_HOME=/home/cgh/.sdkman/candidates/maven/3.5.4
WAD_HOME=~/.wad
APPLICATION_NAME=websockets-notifications

docker image build -t ${APPLICATION_NAME} .
echo "Starting container..."
docker container run -d --rm --name ${APPLICATION_NAME} -p 8080:8080 -v /tmp/wad-deployments:/opt/jboss/wildfly/standalone/deployments ${APPLICATION_NAME}
echo "Watch and Deploy..."
java -jar ${WAD_HOME}/wad.jar /tmp/wad-deployments
echo "Stopping container..."
docker container stop ${APPLICATION_NAME}
