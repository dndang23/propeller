#!/bin/sh

num=$1
date=$2
dir=$3

if [ "${dir}" == "p"  ]; then
	dir="prob_epsilon_lexicase"
elif [ "${dir}" == "d_g" ]; then
	dir="default_epsilon_lexicase_increased_generations"
else
	dir="default_epsilon_lexicase_increased_population"
fi

if [ "${dir}" == "95"  ]; then
	dir="prob_05_95"
elif [ "${dir}" == "80" ]; then
	dir="prob_20_80"
else
	dir="prob_30_70"
fi

for (( i=$num; i<=$num; i++ ))
do
	for (( j=1; j<=100; j++ ))
	do
	  cat /home/dndang23/Desktop/second_propeller/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/${dir}/${j}/err
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

