#!/bin/bash

# Check if text parameter was provided
if [ $# -eq 0 ]; then
    echo "Erro: Por favor forneça o texto para o log"
    echo "Uso: ./toLog.sh \"texto da mensagem\""
    exit 1
fi

# Store the message text
message="$1"

# Get current year-month in YY-MM format
current_file=$(date +%y-%m)

# Check if directory exists in /opt/musica/log, create if it doesn't
if [ ! -d "/opt/musica/log" ]; then
    mkdir -p "/opt/musica/log"
fi

# Write current date and message to log file
#date "+%Y-%m-%d %H:%M:%S" | tr -d '\n' >> "/opt/musica/log/$current_file.log"
#echo " $message" >> "/opt/musica/log/$current_file.log"

# Write current date and message to log file
echo "$(date "+%Y-%m-%d %H:%M:%S") - $message" >> "/opt/musica/log/$current_file.log"