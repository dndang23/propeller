#!/bin/sh

val=${1}
num_1=${2}
num_2=${3}
date_1=${4}
date_2=${5}

cd default_increased_generations

for (( i=${num_1}; i<=${num_2}; i++ ))
do
  sed -i -e "s/default-epsilon-lexicase-increased-generations/probabilistic-20-80/g" symbolic_regression_${val}_run_${i}.slurm
  sed -i -e "s#propeller_dir#second_propeller/propeller_dir#g" symbolic_regression_${val}_run_${i}.slurm
	sed -i -e "s/default_epsilon_lexicase_increased_generations/prob_20_80/g" symbolic_regression_${val}_run_${i}.slurm
	sed -i -e "s/${date_1}/${date_2}/g" symbolic_regression_${val}_run_${i}.slurm
        #sleep 1
done

cd ..

mv default_increased_generations probabilistic-20-80

cd default_increased_population

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e "s/default-epsilon-lexicase-increased-population/probabilistic-30-70/g" symbolic_regression_${val}_run_${i}.slurm
  sed -i -e "s#propeller_dir#second_propeller/propeller_dir#g" symbolic_regression_${val}_run_${i}.slurm
  sed -i -e "s/default_epsilon_lexicase_increased_population/prob_30_70/g" symbolic_regression_${val}_run_${i}.slurm
  sed -i -e "s/${date_1}/${date_2}/g" symbolic_regression_${val}_run_${i}.slurm
        #sleep 1
done

cd ..

mv default_increased_population probabilistic-30-70

cd probabilistic

for (( i=${num_1}; i<=${num_2}; i++ ))
do
	sed -i -e "s/probabilistic-epsilon-lexicase/probabilistic-05-95/g" symbolic_regression_${val}_run_${i}.slurm
	sed -i -e "s#propeller_dir#second_propeller/propeller_dir#g" symbolic_regression_${val}_run_${i}.slurm
	sed -i -e "s/prob_epsilon_lexicase/prob_05_95/g" symbolic_regression_${val}_run_${i}.slurm
	sed -i -e "s/${date_1}/${date_2}/g" symbolic_regression_${val}_run_${i}.slurm
	#sleep 1
done

cd ..

mv probabilistic probabilistic-05-95

