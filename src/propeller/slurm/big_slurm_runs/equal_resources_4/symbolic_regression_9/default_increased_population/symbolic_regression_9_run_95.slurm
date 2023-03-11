#!/bin/bash
#SBATCH --time=168:00:00
#SBATCH --nodes=1
#SBATCH --ntasks=1
#SBATCH --account=dndang23
#SBATCH --partition=cpu-q
#SBATCH -e /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/2023-03-11-17/symbolic_regression_9/default_epsilon_lexicase_increased_population/95/err
#SBATCH -o /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/2023-03-11-17/symbolic_regression_9/default_epsilon_lexicase_increased_population/95/out

ml amh-clojure
lein run -m propeller.problems.symbolic_regression_9 default-epsilon-lexicase-increased-population
