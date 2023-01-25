#!/bin/sh

val=${1}
val_2=${2}
num_1=${3}
num_2=${4}

cd default

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	mv symbolic_regression_${val}_run_${i}.slurm symbolic_regression_${val_2}_run_${i}.slurm
 	sed -i -e "s/symbolic_regression_${val}/symbolic_regression_${val_2}/g" symbolic_regression_${val_2}_run_${i}.slurm
 	#sleep 1
done

cd ..

cd probabilistic

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	mv symbolic_regression_${val}_run_${i}.slurm symbolic_regression_${val_2}_run_${i}.slurm
        sed -i -e "s/symbolic_regression_${val}/symbolic_regression_${val_2}/g" symbolic_regression_${val_2}_run_${i}.slurm
	#sleep 1
done

cd ..

