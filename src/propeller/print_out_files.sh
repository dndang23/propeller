#!/bin/sh

date=$1

echo "Probabilistic plushy output"
for i in {1..3}
do
	for j in {1..5}
	do
		echo "(probabilistic) symbolic_regression_${i}, test ${j}"
		sleep 1
		cat results/${date}/symbolic_regression_${i}/prob/${j}/out
		sleep 5
	done
done

echo ""

echo "Default plushy output"
for i in {1..3}
do
	for j in {1..5}
	do
		echo "(default) symbolic_regression_${i}, test ${j}"
		sleep 1
		cat results/${date}/symbolic_regression_${i}/default/${j}/out
		sleep 5
	done
done

