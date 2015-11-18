#!/bin/bash

sed '/http/d' $1 > $1.temp
