#!/bin/sh

date=$1
START=$2
END=$3
dir=$4

echo "Plushy (epsilon lexicase selection) output"
for (( i=${START}; i<=${END}; i++ ))
do
	for j in {1..100}
	do
		#echo "(probabilistic - lexicase) symbolic_regression_${i}, test ${j}"
		#sleep 1
		git add -f /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/${dir}/${j}/out
		#sleep 1
	done
done

echo ""

#echo "Probabilistic plushy (epsilon-lexicase selection) output"
#for (( i=${START}; i<=${END}; i++ ))
#do
#	for j in {1..100}
#	do
		#echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
		#sleep 1
#		git add -f /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/default_epsilon_lexicase/${j}/out
		#sleep 1
#	done
#done

