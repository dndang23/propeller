#!/bin/sh

for i in {1..3}
do
	
	cd slurm_runs/symbolic_regression_${i}
	./slurm_runs.sh ${i} 1 10
	cd ..
	cd ..
done
