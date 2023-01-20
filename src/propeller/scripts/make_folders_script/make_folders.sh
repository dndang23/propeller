#!/bin/sh

date=$1

for i in {4..7}
do
	for j in {1..100}
	do
		mkdir -p /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/default_epsilon_lexicase/${j}/
		mkdir -p /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/prob_epsilon_lexicase/${j}/
	done
done

touch /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/results.txt


