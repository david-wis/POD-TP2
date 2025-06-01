#!/bin/bash
cd target
tar -xvf tpe2-g4-server-1.0-SNAPSHOT-bin.tar.gz
cd tpe2-g4-server-1.0-SNAPSHOT
chmod u+x ./run-server.sh
docker compose up
