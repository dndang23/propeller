#!/bin/sh

num_1=${1}
num_2=${2}

for (( i=${num_1}; i<=${num_2}; i++ ))
do

	cd symbolic_regression_${i}
	bash change_date.sh ${i} 1 100 2023-03-28-26 2023-03-28-16
	cd ..
done