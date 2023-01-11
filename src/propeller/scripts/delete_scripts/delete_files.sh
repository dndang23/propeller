#!/bin/sh

date=$1

for i in {1..3}
do
	for j in {1..5}
	do
		rm -f results/${date}/symbolic_regression_${i}/prob/${j}/out
		rm -f results/${date}/symbolic_regression_${i}/prob/${j}/log
		rm -f results/${date}/symbolic_regression_${i}/prob/${j}/err

		rm -f results/${date}/symbolic_regression_${i}/default/${j}/out
		rm -f results/${date}/symbolic_regression_${i}/default/${j}/log
		rm -f results/${date}/symbolic_regression_${i}/default/${j}/err
	done
done


