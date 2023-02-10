#!/bin/sh

val=${1}
num_1=${2}
num_2=${3}

cd default_increased_generations

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sbatch symbolic_regression_${val}_run_${i}.slurm
        sleep 1
done

cd ..

cd default_increased_population

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sbatch symbolic_regression_${val}_run_${i}.slurm
        sleep 1
done

cd ..

cd probabilistic

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sbatch symbolic_regression_${val}_run_${i}.slurm
        sleep 1
done

cd ..
