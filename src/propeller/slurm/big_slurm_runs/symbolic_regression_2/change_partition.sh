#!/bin/sh

val=${1}
num_1=${2}
num_2=${3}

cd default

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e 's/gpu-a5000-q/gpu-a100-q/g' symbolic_regression_${val}_run_${i}.slurm
        #sleep 1
done

cd ..

cd probabilistic

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e 's/gpu-a5000-q/gpu-a100-q/g' symbolic_regression_${val}_run_${i}.slurm 
	#sleep 1
done

cd ..

