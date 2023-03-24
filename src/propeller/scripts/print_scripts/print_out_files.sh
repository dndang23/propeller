#!/bin/sh

date=$1
num_seconds=$2
num_seconds_2=$3
start_default=$4
end_default=$5
num=$6
dir=$7
#start_prob=$8
#end_prob=$9

if [ "${dir}" == "p"  ]; then
#	dir="prob"
	dir="prob_epsilon_lexicase"
elif [ "${dir}" == "d_g" ]; then
#	dir="default"
	dir="default_epsilon_lexicase_increased_generations"
else
	dir="default_epsilon_lexicase_increased_population"	
fi	

echo "Plushy (epsilon-lexicase selection) output"
#for i in {5..5}
#do
counter=0
counter_2=0
for (( i=$num; i<=$num; i++ ))
do
	for (( j=$start_default; j<=$end_default; j++ ))
	do
		#echo "(default - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
		#sleep 1
		#last_lines=`tail -15 /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${1}/default_epsilon_lexicase/${j}/out`
		#echo ${last_lines}
		gen_num=`cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/${dir}/${j}/out | grep average_num_generations`
		if [ -z "${gen_num}" ]
		then
			echo "symbolic_regression_${i}, test ${j} is not finished"
			((counter_2=counter_2+1))
			sleep ${num_seconds}
		else
			if [[ "${gen_num}" != *"-1"* ]]; then
				echo "symbolic_regression_${i}, test ${j}"
				echo ${gen_num}
				test_error_num=`cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/${dir}/${j}/out | grep total-test-error`
				echo ${test_error_num}
				
				((counter=counter+1))
				#sleep ${num_seconds_2}
			fi

		fi

		#cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${1}/default_epsilon_lexicase/${j}/out
	done
done

echo "Num successes for ${dir} = ${counter}"
echo "${counter_2} runs left"
#echo ""
#echo ""
#echo ""

#counter_2=0
#echo "Probabilistic plushy (epsilon-lexicase selection) output"
#sleep 5
#for i in {5..5}
#do
#for (( i=$num; i<=$num; i++ ))
#do
#	for (( j=$start_prob; j<=$end_prob; j++ ))
#	do
		#echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
		#sleep 1
#		gen_num=`cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/prob_epsilon_lexicase/${j}/out | grep average_num_generations`
#		if [ -z "${gen_num}" ]
#		then
#			echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j} is not finished"
#			sleep ${num_seconds}
#		else
#			if [[ "${gen_num}" != *"-1"* ]]; then
#				echo "(probabilistic - epsilon-lexicase) symbolic_regression_${i}, test ${j}"
#				echo ${gen_num}
#				test_error_num=`cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${i}/prob_epsilon_lexicase/${j}/out | grep total-test-error`
#				echo ${test_error_num}
#				((counter_2=counter_2+1))
				#sleep ${num_seconds_2}
#			fi
#		fi

		#cat /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/${date}/symbolic_regression_${1}/prob_epsilon_lexicase/${j}/out
#	done
#done

#echo "Num successes for probabilistic epsilon-lexicase selection = ${counter_2}"
echo "Fin"
