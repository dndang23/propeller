#!/bin/sh

for i in {1..12}
do
	condor_submit fizz_buzz_runs_${i}.cmd
        sleep 2
done
