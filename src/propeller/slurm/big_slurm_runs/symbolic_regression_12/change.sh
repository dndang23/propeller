#!/bin/sh

val=${1}
num_1=${2}
num_2=${3}
val_1=${4}
val_2=${5}

cd default

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e "s/${val_1}/${val_2}/g" symbolic_regression_${val}_run_${i}.slurm
        #sleep 1
done

cd ..

cd probabilistic

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e "s/${val_1}/${val_2}/g" symbolic_regression_${val}_run_${i}.slurm
	#sleep 1
done

cd ..

