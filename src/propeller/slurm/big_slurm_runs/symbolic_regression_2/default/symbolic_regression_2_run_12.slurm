#!/bin/bash
#SBATCH --time=168:00:00
#SBATCH --nodes=1
#SBATCH --ntasks=8
#SBATCH --account=dndang23
#SBATCH --partition=gpu-a100-q
#SBATCH -e /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/2023-01-09-22/symbolic_regression_2/default_epsilon_lexicase/12/err
#SBATCH -o /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/2023-01-09-22/symbolic_regression_2/default_epsilon_lexicase/12/out

lein run -m propeller.problems.symbolic_regression_2 default_epsilon_lexicase
