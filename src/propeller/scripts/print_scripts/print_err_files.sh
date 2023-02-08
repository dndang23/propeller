#!/bin/sh

num=$1
date=$2
dir=$3

echo "Plushy (epsilon-lexicase selection) output"
for (( i=$num; i<=$num; i++ ))
do
	for j in {1..100}
	do
		echo "symbolic_regression_${i}, test ${j}"
		# sleep 1
		cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/${dir}/${j}/err
		sleep 0.1
	done
done

#echo ""

#echo "Probabilistic plushy (epsilon-lexicase selection) output"
#for (( i=$num; i<=$num; i++ ))
#do
#	for j in {1..100}
#	do
#		echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
		# sleep 1
#		cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/prob_epsilon_lexicase/${j}/err
#		sleep 0.1
#	done
#done

