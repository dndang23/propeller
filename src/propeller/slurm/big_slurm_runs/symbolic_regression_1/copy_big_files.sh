#!/bin/bash


cd default

for i in {1..100}
do
	cp symbolic_regression_1_run_$i.slurm C:/Users/bomba/Documents/my_propeller/propeller/src/propeller/big_slurm_runs/symbolic_regression_3/default/symbolic_regression_3_run_$i.slurm
done

cd ..

cd probabilistic

for i in {1..100} 
do
	cp symbolic_regression_1_run_$i.slurm C:/Users/bomba/Documents/my_propeller/propeller/src/propeller/big_slurm_runs/symbolic_regression_3/probabilistic/symbolic_regression_3_run_$i.slurm
done

cd ..