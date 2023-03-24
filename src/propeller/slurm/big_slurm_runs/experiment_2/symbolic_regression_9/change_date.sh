#!/bin/sh

val=${1}
num_1=${2}
num_2=${3}
date_1=${4}
date_2=${5}

cd default

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e "s/${date_1}/${date_2}/g" symbolic_regression_${val}_run_${i}.slurm
        #sleep 1
done

cd ..

cd probabilistic

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e "s/${date_1}/${date_2}/g" symbolic_regression_${val}_run_${i}.slurm
	#sleep 1
done

cd ..

