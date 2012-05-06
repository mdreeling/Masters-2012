#NodeJSServerTrack
description "Main Node JS Server - TrackMyGear"
author      "Mick"

start on startup
stop on shutdown

chdir /apps/sites/masters/mwd/trackmygearfor.me

script
    export HOME="/root"

    echo $$ > /var/run/trackmygearfor.me.pid
    exec sudo -u dev node /apps/sites/masters/mwd/trackmygearfor.me/node/lib/db-server-track.js >> /var/log/nodeserver-trackmygearfor.me.sys.log 2>&1
end script

pre-start script
    # Date format same as (new Date()).toISOString() for consistency
    echo "[`date -u +%Y-%m-%dT%T.%3NZ`] (sys) Starting" >> /var/log/trackmygearfor.me.sys.log
end script

pre-stop script
    rm /var/run/trackmygearfor.me.pid
    echo "[`date -u +%Y-%m-%dT%T.%3NZ`] (sys) Stopping" >> /var/log/trackmygearfor.me.sys.log
end script
