#!/bin/sh

ROOT_DIR=$(pwd)

cd node-chat-server
npm install >> $ROOT_DIR/logs/node-chat-server.log 2>&1
npm start >> $ROOT_DIR/logs/node-chat-server.log 2>&1 &
cd ..
