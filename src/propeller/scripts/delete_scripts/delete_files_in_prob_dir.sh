#!/bin/sh
for i in {1..3}
do
	for j in {1..5}
	do
		rm -f results/symbolic_regression_${i}/prob/${j}/err
		rm -f results/symbolic_regression_${i}/prob/${j}/log
		rm -f results/symbolic_regression_${i}/prob/${j}/out
	done
done
