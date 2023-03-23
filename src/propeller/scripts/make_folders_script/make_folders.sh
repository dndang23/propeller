#!/bin/sh

date=$1
START=$2
END=$3

for (( i=${START}; i<=${END}; i++ ))
do
	for j in {1..100}
	do
#		mkdir -p /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/default_epsilon_lexicase_increased_generations/${j}/
#		mkdir -p /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/default_epsilon_lexicase_increased_population/${j}/
		mkdir -p /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/default/${j}/
		mkdir -p /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/prob/${j}/
	done
done

touch /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/results.txt

