#!/usr/bin/env bash
REPOSITORY=/home/ubuntu
PROJECT_NAME=locat-server
DOCKER_REPO=$1

if [ "$(id -u)" -ne 0 ]; then
  echo "Docker deployment script must be run as root!"
  exit 1
fi

if [ "$PWD" != "$REPOSITORY" ]; then
  cd $REPOSITORY || { echo "Failed to change directory to $REPOSITORY. Exiting..."; exit 1; }
fi

echo "Start deploying $PROJECT_NAME with Docker"
echo "Stopping & Removing existing container..."
docker stop $PROJECT_NAME || true
docker rm $PROJECT_NAME || true
echo "> Done!"

echo "Pulling latest image from Docker Hub($DOCKER_REPO)..."
docker pull "$DOCKER_REPO":latest
echo "> Done!"

echo "Running a new Docker container"
docker run -d -p 443:443 --name $PROJECT_NAME "$DOCKER_REPO":latest
echo "> Done!"

echo "Pruning unused Docker images"
docker image prune -f
echo "> Done!"

echo "Deploy process has been completed!"
