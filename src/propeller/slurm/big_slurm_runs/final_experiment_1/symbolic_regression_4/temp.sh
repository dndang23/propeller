#!/bin/sh

val=${1}
num_1=${2}
num_2=${3}
date_1=${4}
date_2=${5}

#cd default_increased_generations

#sed -i "10i ml amh-clojure" symbolic_regression_*

#for (( i=${num_1}; i<=${num_2}; i++ ))
#do
#	sed -i -e "s/${date_1}/${date_2}/g" symbolic_regression_${val}_run_${i}.slurm
        #sleep 1
#done

#cd ..

cd default_increased_population

#sed -i "10i ml amh-clojure" symbolic_regression_*

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e "s/${date_1}/${date_2}/g" symbolic_regression_${val}_run_${i}.slurm
        #sleep 1
done

#cd ..

#cd probabilistic

#sed -i "10i ml amh-clojure" symbolic_regression_*

#for (( i=${num_1}; i<=${num_2}; i++ ))
#do
#	sed -i -e "s/${date_1}/${date_2}/g" symbolic_regression_${val}_run_${i}.slurm
	#sleep 1
#done

cd ..

