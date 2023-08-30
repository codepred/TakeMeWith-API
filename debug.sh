#git pull
#export JAVA_HOME=/usr/lib/jvm/java-1.11.0-openjdk-amd64
mvn jasypt:decrypt -Djasypt.encryptor.password=CodePredXXAA12
mvn clean install -Djasypt.encryptor.password=CodePredXXAA12
docker-compose down
clear

set -a
source .my-env
docker-compose up --build