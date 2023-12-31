#!/bin/sh

ROOT_DIR=$(pwd)

mkdir logs
touch logs/node-chat-server.log

cd node-chat-server
npm install 2>&1 | tee $ROOT_DIR/logs/node-chat-server.log
npm start 2>&1 | tee $ROOT_DIR/logs/node-chat-server.log

