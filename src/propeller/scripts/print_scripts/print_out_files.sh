#!/bin/sh

num=$1
date=$2
num_seconds=$3
num_seconds_2=$4
start_default=$5
end_default=$6
start_prob=$7
end_prob=$8

echo "Default plushy (epsilon-lexicase selection) output"
#for i in {5..5}
#do
counter=0
for (( i=$num; i<=$num; i++ ))
do
	for (( j=$start_default; j<=$end_default; j++ ))
	do
		#echo "(default - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
		#sleep 1
		#last_lines=`tail -15 /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${1}/default_epsilon_lexicase/${j}/out`
		#echo ${last_lines}
		gen_num=`cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/default_epsilon_lexicase/${j}/out | grep average_num_generations`
		if [ -z "${gen_num}" ]
		then
			echo "(default - epsilon-lexicase) symbolic_regression_${i}, test ${j} is not finished"
			sleep ${num_seconds}
		else
			if [[ "${gen_num}" != *"-1"* ]]; then
				echo "(default - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
				echo ${gen_num}
				test_error_num=`cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/default_epsilon_lexicase/${j}/out | grep total-test-error`
				echo ${test_error_num}
				
				((counter=counter+1))
				#sleep ${num_seconds_2}
			fi

		fi

		#cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${1}/default_epsilon_lexicase/${j}/out
	done
done

echo "Num successes for default epsilon-lexicase selection = ${counter}"
echo ""
echo ""
echo ""

counter_2=0
echo "Probabilistic plushy (epsilon-lexicase selection) output"
#sleep 5
#for i in {5..5}
#do
for (( i=$num; i<=$num; i++ ))
do
	for (( j=$start_prob; j<=$end_prob; j++ ))
	do
		#echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
		#sleep 1
		gen_num=`cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/prob_epsilon_lexicase/${j}/out | grep average_num_generations`
		if [ -z "${gen_num}" ]
		then
			echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j} is not finished"
			sleep ${num_seconds}
		else
			if [[ "${gen_num}" != *"-1"* ]]; then
				echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
				echo ${gen_num}
				test_error_num=`cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/prob_epsilon_lexicase/${j}/out | grep total-test-error`
				echo ${test_error_num}
				((counter_2=counter_2+1))
				#sleep ${num_seconds_2}
			fi
		fi

		#cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${1}/prob_epsilon_lexicase/${j}/out
	done
done

echo "Num successes for probabilistic epsilon-lexicase selection = ${counter_2}"
echo "Fin"
