#!/bin/sh

sym_num_1=${1}
sym_num_2=${2}
num_1=${3}
num_2=${4}

dir_1="default_increased_generations"
dir_2="default_increased_population"
dir_3="probabilistic"

cp -r "symbolic_regression_${sym_num_1}" "symbolic_regression_${sym_num_2}"

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	mv symbolic_regression_${sym_num_2}/${dir_1}/symbolic_regression_${sym_num_1}_run_${i}.slurm symbolic_regression_${sym_num_2}/${dir_1}/symbolic_regression_${sym_num_2}_run_${i}.slurm
	sed -i -e "s/symbolic_regression_${sym_num_1}/symbolic_regression_${sym_num_2}/g" symbolic_regression_${sym_num_2}/${dir_1}/symbolic_regression_${sym_num_2}_run_${i}.slurm	
	
	mv symbolic_regression_${sym_num_2}/${dir_2}/symbolic_regression_${sym_num_1}_run_${i}.slurm symbolic_regression_${sym_num_2}/${dir_2}/symbolic_regression_${sym_num_2}_run_${i}.slurm
	sed -i -e "s/symbolic_regression_${sym_num_1}/symbolic_regression_${sym_num_2}/g" symbolic_regression_${sym_num_2}/${dir_2}/symbolic_regression_${sym_num_2}_run_${i}.slurm	
	
	mv symbolic_regression_${sym_num_2}/${dir_3}/symbolic_regression_${sym_num_1}_run_${i}.slurm symbolic_regression_${sym_num_2}/${dir_3}/symbolic_regression_${sym_num_2}_run_${i}.slurm
	sed -i -e "s/symbolic_regression_${sym_num_1}/symbolic_regression_${sym_num_2}/g" symbolic_regression_${sym_num_2}/${dir_3}/symbolic_regression_${sym_num_2}_run_${i}.slurm	
	
	#sleep 1
done

#cd default_increased_population

#for (( i=${num_1}; i<=${num_2}; i++ ))
#do
#	sed -i -e "s/${prev}/${new}/g" symbolic_regression_${val}_run_${i}.slurm
#	#sleep 1
#done

#cd ..

