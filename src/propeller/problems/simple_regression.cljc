(ns propeller.problems.simple-regression
  (:require [propeller.genome :as genome]
            [propeller.push.interpreter :as interpreter]
            [propeller.push.state :as state]
            [propeller.tools.math :as math]
            [propeller.gp :as gp]
            #?(:cljs [cljs.reader :refer [read-string]])))

(defn- target-function
  "Target function: f(x) = x^3 + x + 3"
  [x]
  (+ (* x x x) x 3))

(def train-and-test-data
  (let [train-inputs (range -10 11)
        test-inputs (concat (range -20 -10) (range 11 21))]
    {:train (map (fn [x] {:input1 (vector x) :output1 (vector (target-function x))}) train-inputs)
     :test (map (fn [x] {:input1 (vector x) :output1 (vector (target-function x))}) test-inputs)}))

(def instructions
  (list :in1
        :integer_add
        :integer_subtract
        :integer_mult
        :integer_quot
        :integer_eq
        :exec_dup
        :exec_if
        'close
        0
        1))

(defn error-function
  "Finds the behaviors and errors of an individual. The error is the absolute
  deviation between the target output value and the program's selected behavior,
  or 1000000 if no behavior is produced. The behavior is here defined as the
  final top item on the INTEGER stack."
  ([argmap data individual]
   (let [program (genome/plushy->push (:plushy individual) argmap)
         inputs (map (fn [x] (first (:input1 x))) data)
         correct-outputs (map (fn [x] (first (:output1 x))) data)
         outputs (map (fn [input]
                        (state/peek-stack
                          (interpreter/interpret-program
                            program
                            (assoc state/empty-state :input {:in1 input})
                            (:step-limit argmap))
                          :integer))
                      inputs)
         errors (map (fn [correct-output output]
                       (if (= output :no-stack-item)
                         1000000
                         (math/abs (- correct-output output))))
                     correct-outputs
                     outputs)]
     (assoc individual
       :behaviors outputs
       :errors errors
       :total-error #?(:clj  (apply +' errors)
                       :cljs (apply + errors))))))

(defn -main
  "Runs propel-gp, giving it a map of arguments."
  [& args]

  (loop [num_tries 0 num_successes 0 num_generations 0 generations_list []]
    (if (= num_tries 12)
      (do (println "Results of run")
          (prn {:percent_of_successes (float (/ num_successes num_tries))})
          (prn {:average_num_generations (float (/ num_generations num_successes))})
          (prn {:list_of_successful_generations generations_list})
          (println ))
      (do
        (println "Beginning run number" (inc num_tries))
        (println )
        (let [output   (gp/gp
                         (merge
                           {:instructions             instructions
                            :error-function           error-function
                            :training-data            (:train train-and-test-data)
                            :testing-data             (:test train-and-test-data)
                            :max-generations          500
                            :population-size          500
                            :max-initial-plushy-size  100
                            :step-limit               200
                            :parent-selection         :lexicase
                            :tournament-size          5
                            :umad-rate                0.1
                            :variation                 {:umad 0.5 :crossover 0.5}
                            :elitism                  false}
                           (apply hash-map (map #(if (string? %) (read-string %) %) args))))
              val  (if (nil? output)
                     {:success-generation? 0 :num-generations 0}
                     {:success-generation? 1 :num-generations (:success-generation output)})
              updated_list (if (nil? output)
                             generations_list
                             (conj generations_list (:success-generation output)))]
          (println "Statistics based on current number of runs: ")
          (prn {:num_successes (+ num_successes (:success-generation? val))})
          (prn {:num_tries (inc num_tries)})
          (prn {:total_num_generations (+ num_generations (:num-generations val))})
          (prn {:list_of_successful_generations updated_list})
          (println )
          (recur (inc num_tries) (+ num_successes (:success-generation? val)) (+ num_generations (:num-generations val)) updated_list))))))

;goal 1: run some tests on the probabilistic approach and see if it can solve the problem (print the final program)
;goal 2: compare the performance with the standard approach