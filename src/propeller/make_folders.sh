#!/bin/sh

date=$1

for i in {1..3}
do
	for j in {1..5}
	do
		mkdir -p results/${date}/symbolic_regression_${i}/prob_lexicase/${j}/
		mkdir -p results/${date}/symbolic_regression_${i}/prob_tournament/${j}/
	done
done

touch results/${date}/results.txt


