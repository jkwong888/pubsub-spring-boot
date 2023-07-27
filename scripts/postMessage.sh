#!/bin/bash

URL=https://topic-router-qv24sofnbq-uc.a.run.app
#URL=http://localhost:8080
TOPIC_IDX=$(shuf -i 0-0 -n 1)


for i in `seq 0 100`; do
    cat << EOF | curl -i -X POST -H "X-Topic-Name: topic${TOPIC_IDX}" -H "Content-Type: application/json" -d@- ${URL}/postMessage
{
    "username": "jkwng",
    "body": "This is a message on topic ${TOPIC_IDX}"
}
EOF
/bin/sleep 1
done
