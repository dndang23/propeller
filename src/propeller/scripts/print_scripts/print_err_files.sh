#!/bin/sh

date=$1

echo "Probabilistic plushy (lexicase selection) output"
for i in {5..5}
do
	for j in {1..100}
	do
		echo "(probabilistic - lexicase) symbolic_regression_${i}, test ${j}"
		# sleep 1
		cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/default_epsilon_lexicase/${j}/err
		sleep 0.1
	done
done

echo ""

echo "Probabilistic plushy (epsilon-lexicase selection) output"
for i in {5..5}
do
	for j in {1..100}
	do
		echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
		# sleep 1
		cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/prob_epsilon_lexicase/${j}/err
		sleep 1
	done
done

