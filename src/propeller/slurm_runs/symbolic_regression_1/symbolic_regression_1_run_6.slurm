#!/bin/bash
#SBATCH --time=1:00:00
#SBATCH --nodes=1
#SBATCH --ntasks=8
#SBATCH --account=dndang23
#SBATCH --partition=gpu-a5000-q
#SBATCH -e /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/2022-12-28-14/symbolic_regression_1/prob_epsilon_lexicase/1/err
#SBATCH -o /home/dndang23/Desktop/propeller_dir/propeller/src/propeller/results/2022-12-28-14/symbolic_regression_1/prob_epsilon_lexicase/1/out

lein run -m propeller.problems.symbolic_regression_1 default

