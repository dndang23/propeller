#!/bin/sh

dir=${1}
val=${2}
num_1=${3}
num_2=${4}
prev=${5}
new=${6}


for (( i=${num_1}; i<=${num_2}; i++ ))
do
	
	sed -i -e "s/${prev}/${new}/g" ${dir}/symbolic_regression_${val}_run_${i}.slurm
	#sleep 1
done

#cd default_increased_population

#for (( i=${num_1}; i<=${num_2}; i++ ))
#do
#	sed -i -e "s/${prev}/${new}/g" symbolic_regression_${val}_run_${i}.slurm
#	#sleep 1
#done

#cd ..

