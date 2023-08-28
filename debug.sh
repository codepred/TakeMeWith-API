git pull
export JAVA_HOME=/usr/lib/jvm/java-1.11.0-openjdk-amd64
mvn clean install
docker-compose down
clear
docker-compose up --build