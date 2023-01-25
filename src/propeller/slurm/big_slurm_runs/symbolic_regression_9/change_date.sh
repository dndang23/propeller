#!/bin/sh

val=${1}
num_1=${2}
num_2=${3}

cd default

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e 's/2023-01-19-19/2023-01-24-20/g' symbolic_regression_${val}_run_${i}.slurm
        #sleep 1
done

cd ..

cd probabilistic

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e 's/2023-01-19-19/2023-01-24-20/g' symbolic_regression_${val}_run_${i}.slurm
	#sleep 1
done

cd ..

