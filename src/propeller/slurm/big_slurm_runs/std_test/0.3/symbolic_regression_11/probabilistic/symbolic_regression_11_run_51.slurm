#!/bin/bash
#SBATCH --time=168:00:00
#SBATCH --nodes=1
#SBATCH --ntasks=1
#SBATCH --account=dndang23
#SBATCH --partition=cpu-q
#SBATCH -e /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/2023-02-19-05/symbolic_regression_11/prob_epsilon_lexicase/51/err
#SBATCH -o /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/2023-02-19-05/symbolic_regression_11/prob_epsilon_lexicase/51/out

lein run -m propeller.problems.symbolic_regression_11 probabilistic-epsilon-lexicase
