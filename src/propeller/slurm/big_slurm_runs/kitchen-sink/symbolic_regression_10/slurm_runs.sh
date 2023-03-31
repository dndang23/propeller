#!/bin/sh

val=${1}
num_1=${2}
num_2=${3}

cd probabilistic-30-70

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sbatch symbolic_regression_${val}_run_${i}.slurm
        sleep 1
done

cd ..

cd probabilistic-20-80

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sbatch symbolic_regression_${val}_run_${i}.slurm
        sleep 1
done

cd ..

cd probabilistic-05-95

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sbatch symbolic_regression_${val}_run_${i}.slurm
        sleep 1
done

cd ..
