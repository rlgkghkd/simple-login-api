#!/bin/bash
set -e # Any command failure will exit the script immediately

echo "--- Starting SSH Script ---"
echo "Current directory on EC2: $(pwd)"
echo "EC2_APP_PATH: /home/ec2-user/app" # 실제 EC2_APP_PATH 값으로 변경해주세요
echo "JAR_FILE_NAME: SimpleLoginAPI-0.0.1-SNAPSHOT.jar" # 실제 JAR_FILE_NAME 값으로 변경해주세요

echo "Stopping existing application..."
pkill -f 'java -jar'
PKILL_EXIT_CODE=$?
echo "pkill exit code: $PKILL_EXIT_CODE"
if [ $PKILL_EXIT_CODE -eq 0 ]; then
    echo "Existing application terminated."
    sleep 5 # 프로세스 종료 대기
elif [ $PKILL_EXIT_CODE -eq 1 ]; then
    echo "No existing application found to terminate."
else
    echo "pkill command failed with unexpected exit code: $PKILL_EXIT_CODE"
fi

echo "Creating application directory if it doesn't exist..."
mkdir -p /home/ec2-user/app # 실제 EC2_APP_PATH 값으로 변경해주세요
MKDIR_EXIT_CODE=$?
echo "mkdir exit code: $MKDIR_EXIT_CODE"
if [ $MKDIR_EXIT_CODE -ne 0 ]; then
    echo "Error creating directory. Exiting."
    exit $MKDIR_EXIT_CODE
fi

echo "Starting new application..."
nohup java -jar /home/ec2-user/app/SimpleLoginAPI-0.0.1-SNAPSHOT.jar > /home/ec2-user/app/app.log 2>&1 &
NOHUP_EXIT_CODE=$?
echo "nohup command exit code: $NOHUP_EXIT_CODE"
if [ $NOHUP_EXIT_CODE -ne 0 ]; then
    echo "Error starting application with nohup. Exiting."
    exit $NOHUP_EXIT_CODE
fi

sleep 1 # 백그라운드 프로세스 분리 대기
echo "Deployment complete!"
echo "--- SSH Script Finished ---"
