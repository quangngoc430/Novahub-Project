#!/bin/bash

kill $(ps aux | grep '[c]urrent/helpdesk.jar' | awk '{print $2}')
