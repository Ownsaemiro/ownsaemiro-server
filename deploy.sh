#!/bin/bash
./gradlew clean build -x test
docker buildx build --platform linux/amd64 --load --tag dlawjddn/ownsaemiro-server:0.0.1 .
docker push dlawjddn/ownsaemiro-server:0.0.1