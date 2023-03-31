#!/bin/sh

num_1=$1
num_2=$2

cd ..
cd ..
cd slurm

for (( i=${num_1}; i<=${num_2}; i++ ))
do
cd big_slurm_runs/kitchen-sink/symbolic_regression_${i}
./slurm_runs.sh ${i} 1 100
cd ..
cd ..
cd ..
done