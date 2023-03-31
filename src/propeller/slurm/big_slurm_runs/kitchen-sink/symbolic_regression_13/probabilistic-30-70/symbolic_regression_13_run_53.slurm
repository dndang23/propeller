#!/bin/bash
#SBATCH --time=168:00:00
#SBATCH --nodes=1
#SBATCH --ntasks=1
#SBATCH --account=dndang23
#SBATCH --partition=cpu-q
#SBATCH -e /home/dndang23/Desktop/second_propeller/propeller_dir/propeller/src/propeller/results/2023-03-31-05/symbolic_regression_13/prob_30_70/53/err
#SBATCH -o /home/dndang23/Desktop/second_propeller/propeller_dir/propeller/src/propeller/results/2023-03-31-05/symbolic_regression_13/prob_30_70/53/out

ml amh-clojure
lein run -m propeller.problems.symbolic_regression_13 probabilistic-30-70
