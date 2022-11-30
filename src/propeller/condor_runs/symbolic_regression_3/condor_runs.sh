#!/bin/sh

num_1=${1}
num_2=${2}

for i in {${num_1}..${num_2}}
do
	condor_submit symbolic_regression_3_runs_${i}.cmd
        sleep 2
done
