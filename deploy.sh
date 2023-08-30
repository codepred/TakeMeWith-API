mvn jasypt:decrypt -Djasypt.encryptor.password=CodePredXXAA12
mvn clean install -Djasypt.encryptor.password=CodePredXXAA12
docker-compose down
clear

set -a
source .my-env
nohup docker-compose up --build &