#!/bin/sh

date=$1

echo "Probabilistic plushy (lexicase selection) output"
for i in {1..3}
do
	for j in {1..5}
	do
		#echo "(probabilistic - lexicase) symbolic_regression_${i}, test ${j}"
		#sleep 1
		git add -f results/${date}/symbolic_regression_${i}/prob_lexicase/${j}/out
		#sleep 1
	done
done

echo ""

echo "Probabilistic plushy (epsilon-lexicase selection) output"
for i in {1..3}
do
	for j in {1..5}
	do
		#echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
		#sleep 1
		git add -f results/${date}/symbolic_regression_${i}/prob_tournament/${j}/out
		#sleep 1
	done
done

