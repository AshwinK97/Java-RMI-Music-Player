#!/bin/bash

javac *.java && java -Djava.security.policy=policy.txt FileServer
