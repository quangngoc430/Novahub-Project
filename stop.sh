#!/bin/bash
if pgrep current/helpdesk.jar
then
    kill $(ps aux | grep '[c]urrent/helpdesk.jar' | awk '{print $2}')
fi