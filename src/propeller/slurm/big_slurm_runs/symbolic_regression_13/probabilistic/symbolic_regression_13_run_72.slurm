#!/bin/bash
#SBATCH --time=168:00:00
#SBATCH --nodes=1
#SBATCH --ntasks=1
#SBATCH --account=dndang23
#SBATCH --partition=cpu-q
#SBATCH -e /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/2023-01-25-23/symbolic_regression_13/prob_epsilon_lexicase/72/err
#SBATCH -o /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/2023-01-25-23/symbolic_regression_13/prob_epsilon_lexicase/72/out

lein run -m propeller.problems.symbolic_regression_13 probabilistic-epsilon-lexicase
