#!/bin/sh

cd ..
cd ..
cd slurm

#for i in {1..3}
#do
cd big_slurm_runs/symbolic_regression_5
./slurm_runs.sh 5 1 100
cd ..
cd ..
#done
