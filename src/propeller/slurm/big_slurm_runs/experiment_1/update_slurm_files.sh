#!/bin/sh

num_1=${1}
num_2=${2}

for (( i=${num_1}; i<=${num_2}; i++ ))
do

	cd symbolic_regression_${i}
#	bash change_date.sh ${i} 1 100 2023-03-14-21 2023-03-23-04
#	bash change_date.sh ${i} 1 100 default_epsilon_lexicase_increased_generations default
  bash change_date.sh ${i} 1 100 prob_epsilon_lexicase prob
	cd ..
done