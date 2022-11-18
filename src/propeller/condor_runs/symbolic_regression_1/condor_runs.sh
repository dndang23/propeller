#!/bin/sh

for i in {1..5}
do
	condor_submit symbolic_regression_1_runs_${i}.cmd
        sleep 2
done
