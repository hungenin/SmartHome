@echo Off
call mvn clean package -DskipTests
docker build -t tv-guide .
pause