#!/bin/sh

date=$1

echo "Default plushy (epsilon-lexicase selection) output"
for i in {3..3}
do
	for j in {1..100}
	do
		echo "(default - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
		sleep 2
		cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/default_epsilon_lexicase/${j}/out
		sleep 3
	done
done

echo ""

echo "Probabilistic plushy (epsilon-lexicase selection) output"
sleep 5
for i in {3..3}
do
	for j in {1..100}
	do
		echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
		sleep 2
		cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/prob_epsilon_lexicase/${j}/out
		sleep 3
	done
done

