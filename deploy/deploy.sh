#!/usr/bin/env bash
REPOSITORY=/home/ubuntu
PROJECT_NAME=locat-server

echo "> Check if application is already running..."
CURRENT_PID=$(pgrep -rf ${PROJECT_NAME}.*.jar)
if [ -z "$CURRENT_PID" ]; then
  echo ">> No running application found"
else
  echo ">> Found a running application (PID: $CURRENT_PID)"
  kill -15 "$CURRENT_PID"
  echo ">> Sent termination signal to the application and wait 5 seconds..."
  sleep 5
  echo ">>> Done"
fi

echo "> Pulling changes from remote repository..."
if [ -d "$REPOSITORY/$PROJECT_NAME" ]; then
  cd $REPOSITORY/$PROJECT_NAME || { echo "> Failed CD to project root directory"; exit 1; }
  git pull
  echo ">> Done"
fi

echo "> Building artifact with Gradle"
./gradlew clean build -x test
echo ">> Done"

echo "> Copying artifact to the repository root directory"
ARTIFACT=$(find $REPOSITORY/$PROJECT_NAME/build/libs -type f -name "*.jar" ! -name "*-plain.jar")
if [ -z "$ARTIFACT" ]; then
  echo ">> No valid artifact found in build paths"
  exit 1
fi
cp "$ARTIFACT" $REPOSITORY/
echo ">> Done"

echo "> Deploy new application"
JAR_NAME=$(basename "$ARTIFACT")
nohup java -jar $REPOSITORY/"$JAR_NAME" --spring.profiles.active=prod 2>&1 &
echo ">> Deploy process has been completed!"
