(ns propeller.problems.symbolic_regression_1
  (:require [propeller.genome :as genome]
            [propeller.push.interpreter :as interpreter]
            [propeller.push.state :as state]
            [propeller.tools.math :as math]
            [propeller.gp :as gp]
            #?(:cljs [cljs.reader :refer [read-string]])))

(defn- target-function
  "Target function: f(x) = x^4 + x^3 + x^2 + x"
  [x]
  (+ (* x x x x) (* x x x) (* x x) x))

;(range -1.0 0.9 0.1)
;no testing set

(def train-and-test-data
  (let [train-inputs (range -1.0 0.9 0.1)]
    {:train (map (fn [x] {:input1 (vector x) :output1 (vector (target-function x))}) train-inputs)}))

;(print (first (:train train-and-test-data)))

(def instructions
  (list 0
        1
        2
        :in1
        :float_add
        :float_subtract
        :float_mult
        :float_quot
        :float_cos
        :float_sin))

(defn plushy-with-prob->plushy
  [plushy-with-prob]
  ;(println plushy-with-prob)
  (filter identity (map (fn [[thing prob]]
                          (if (< (rand) prob)
                            [thing true]
                            [thing false]))
                        plushy-with-prob)))

;(plushy-with-prob->plushy '([1 1] [:integer_add 1] [:integer_mult 1]))


(defn plushy-with-prob->plushy_2
  [plushy-with-prob]
  ;(println plushy-with-prob)
  (filter identity (map (fn [[thing boolean]]
                          (if (= boolean true)
                            thing
                            nil))
                        plushy-with-prob)))

;(plushy-with-prob->plushy_2 (plushy-with-prob->plushy '([1 0] [:integer_add 0] [:integer_mult 0])))

(defn error-function
  "Finds the behaviors and errors of an individual. The error is the absolute
  deviation between the target output value and the program's selected behavior,
  or 1000000 if no behavior is produced. The behavior is here defined as the
  final top item on the INTEGER stack."
  ([argmap data individual]
   (let [boolean_plushy (plushy-with-prob->plushy (:plushy individual))
         regular_plushy (plushy-with-prob->plushy_2 boolean_plushy)
         program (genome/plushy->push regular_plushy argmap)
         inputs (map (fn [x] (first (:input1 x))) data)
         correct-outputs (map (fn [x] (first (:output1 x))) data)
         outputs (map (fn [input]
                        (state/peek-stack
                          (interpreter/interpret-program
                            program
                            (assoc state/empty-state :input {:in1 input})
                            (:step-limit argmap))
                          :float))
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
                       :cljs (apply + errors))
       :boolean-plushy boolean_plushy
       :program program))))

;giant vectors of outputs and errors
;pick minimum total error
(defn multiple-evaluation-function
  [argmap data individual]
  (loop [i 0 limit 5 min_behaviors_list '() min_error_list '() min_total_error 2147483647 min_program '() min-program-boolean-plushy '()]
    (if (= i limit)
      (assoc individual
        :behaviors min_behaviors_list
        :errors min_error_list
        :min-program-boolean-plushy min-program-boolean-plushy
        :program min_program
        :total-error min_total_error)
      (let [error_map (error-function argmap data individual)
            behaviors (:behaviors error_map)
            errors (:errors error_map)
            total_error (:total-error error_map)
            boolean-plushy (:boolean-plushy error_map)
            program (:program error_map)]
        (if (< total_error min_total_error)
          (recur (inc i) limit behaviors errors total_error program boolean-plushy)
          (recur (inc i) limit min_behaviors_list min_error_list min_total_error min_program min-program-boolean-plushy))))))

(defn -main
  "Runs propel-gp, giving it a map of arguments."
  [& args]

  (loop [num_tries 0 num_successes 0 num_generations 0 generations_list []]
    (if (= num_tries 1)
      (if (= num_successes 0)
        (do (println "Results of run")
            (prn {:percent_of_successes (float (/ num_successes num_tries))})
            (prn {:average_num_generations -1})
            (prn {:list_of_successful_generations generations_list})
            (println ))
        (do (println "Results of run")
            (prn {:percent_of_successes (float (/ num_successes num_tries))})
            (prn {:average_num_generations (float (/ num_generations num_successes))})
            (prn {:list_of_successful_generations generations_list})
            (println )))
      (do
        (println "Beginning run number" (inc num_tries))
        (println )
        (let [output   (gp/gp
                         (merge
                           {:instructions             instructions
                            :error-function           multiple-evaluation-function
                            :training-data            (:train train-and-test-data)
                            :testing-data             nil
                            :max-generations          500
                            :population-size          500
                            :max-initial-plushy-size  100
                            :solution-error-threshold 0.1
                            :step-limit               200
                            :parent-selection         :lexicase
                            :tournament-size          5
                            :umad-rate                0.1
                            ;:variation                 {:umad 0.5 :crossover 0.5}
                            :variation                {:umad-prob 0.30 :best-plushy-prob-mutation-prob 0.70 :crossover 0.0}
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