#!/bin/sh

for i in {1..12}
do
	condor_submit gcd_runs_${i}.cmd
        sleep 2
done
