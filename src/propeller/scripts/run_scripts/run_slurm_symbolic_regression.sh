#!/bin/sh

cd ..
cd ..
cd slurm

#for i in {1..3}
#do
cd big_slurm_runs/symbolic_regression_3
./slurm_runs.sh 3 1 100
cd ..
cd ..
#done
