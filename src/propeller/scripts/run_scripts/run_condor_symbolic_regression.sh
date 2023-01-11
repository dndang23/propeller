#!/bin/sh

for i in {1..3}
do
	
	cd condor_runs/symbolic_regression_${i}
	./condor_runs.sh ${i} 1 10
	cd ..
	cd ..
done
