#!/bin/sh

val=${1}
num_1=${2}
num_2=${3}

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	condor_submit symbolic_regression_${val}_runs_${i}.cmd
        sleep 1
done
