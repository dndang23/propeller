(ns propeller.problems.symbolic_regression_5
  (:require [propeller.genome :as genome]
            [propeller.push.interpreter :as interpreter]
            [propeller.push.state :as state]
            [propeller.tools.math :as math]
            [propeller.gp :as gp]
            #?(:cljs [cljs.reader :refer [read-string]])))

(defn- target-function
  "Target function: f(x) = 10/(5 + SUM_i=1^5 (x_i - 3)^2)"
  [x1 x2 x3 x4 x5]
  (->> (list x1 x2 x3 x4 x5)
       (map #(* (- % 3) (- % 3)))
       (apply +)
       (+ 5)
       (/ 10)))

; (target-function 3 7 3 3 3)

(defn map-vals-input
  "Returns all the input values of a map"
  [i]
  (vals (select-keys i [:input1 :input2 :input3 :input4 :input5])))


(defn get-random-input
  "returns a random input between two ranges"
  [a b]
  (->> (rand)
       (* (- b a))
       (+ a)
       (float)))

(def train-data
  (repeatedly 1024 (fn [] (repeatedly 5 #(get-random-input 0.05 6.05)))))

(def test-data
  (repeatedly 5000 (fn [] (repeatedly 5 #(get-random-input -0.25 6.35)))))

(def train-and-test-data
  (let [train-inputs train-data
        test-inputs test-data]
    {:train (map (fn [x] {:input1 (first x)
                          :input2 (nth x 1)
                          :input3 (nth x 2)
                          :input4 (nth x 3)
                          :input5 (nth x 4)
                          :output1 (apply target-function (map #(nth x %) (range 5)))}) train-inputs)
     :test (map (fn [x] {:input1 (first x)
                         :input2 (nth x 1)
                         :input3 (nth x 2)
                         :input4 (nth x 3)
                         :input5 (nth x 4)
                         :output1 (apply target-function (map #(nth x %) (range 5)))}) test-inputs)}))

(def instructions
  (list :in1
        :in2
        :in3
        :in4
        :in5
        :float_add
        :float_subtract
        :float_mult
        :float_quot
        :float_eq
        :exec_dup
        :exec_if
        'close
        0.0
        1.0))

(def data (:train train-and-test-data))

(defn error-function
  "Finds the behaviors and errors of an individual. The error is the absolute
  deviation between the target output value and the program's selected behavior,
  or 1000000 if no behavior is produced. The behavior is here defined as the
  final top item on the FLOAT stack."
  ([argmap data individual]
   (let [program (genome/plushy->push (:plushy individual) argmap)
         inputs (map (fn [i] (map-vals-input i)) data)
         correct-outputs (map (fn [x] (:output1 x)) data)
         outputs (map (fn [input]
                        (state/peek-stack
                          (interpreter/interpret-program
                            program
                            (assoc state/empty-state :input {:in1 (nth input 0)
                                                             :in2 (nth input 1)
                                                             :in3 (nth input 2)
                                                             :in4 (nth input 3)
                                                             :in5 (nth input 4)})
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
                       :cljs (apply + errors))))))

(defn error-function-2
  "Finds the behaviors and errors of an individual. The error is the absolute
  deviation between the target output value and the program's selected behavior,
  or 1000000 if no behavior is produced. The behavior is here defined as the
  final top item on the INTEGER stack."
  ([argmap data individual]
   (let [boolean_plushy (genome/plushy-with-prob->plushy (:plushy individual))
         regular_plushy (genome/plushy-with-prob->plushy_2 boolean_plushy)
         program (genome/plushy->push regular_plushy argmap)
         inputs (map (fn [i] (map-vals-input i)) data)
         correct-outputs (map (fn [x] (:output1 x)) data)
         outputs (map (fn [input]
                        (state/peek-stack
                          (interpreter/interpret-program
                            program
                            (assoc state/empty-state :input {:in1 (nth input 0)
                                                             :in2 (nth input 1)
                                                             :in3 (nth input 2)
                                                             :in4 (nth input 3)
                                                             :in5 (nth input 4)})
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

(defn multiple-evaluation-function
  [argmap data individual]
  (loop [i 0 limit 10 min_behaviors_list '() min_error_list '() min_total_error 2147483647 min_program '() min-program-boolean-plushy '()]
    (if (= i limit)
      (assoc individual
        :behaviors min_behaviors_list
        :errors min_error_list
        :min-program-boolean-plushy min-program-boolean-plushy
        :program min_program
        :total-error min_total_error)
      (let [error_map (error-function-2 argmap data individual)
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
  [type & args]

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
        (if (= type "probabilistic-epsilon-lexicase")
          (let [output   (gp/gp
                           (merge
                             {:instructions             instructions
                              :error-function           multiple-evaluation-function
                              :error-function_2         error-function
                              :training-data            (:train train-and-test-data)
                              :testing-data             (:test train-and-test-data)
                              :max-generations          500
                              :population-size          500
                              :max-initial-plushy-size  100
                              :solution-error-threshold 0.1
                              :step-limit               200
                              ;:parent-selection         :lexicase
                              :parent-selection         :epsilon-lexicase
                              :tournament-size          5
                              :umad-rate                0.1
                              ;:variation                {:umad 0.5 :crossover 0.5}
                              :variation                {:umad-prob 0.05 :adjusted-plushy-mutation-prob 0.95}
                              ;:variation                {:umad-prob 0.30 :adjusted-plushy-mutation-prob 0.70 :crossover 0.0}
                              :elitism                  false
                              :isDefault                false}
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
            (recur (inc num_tries) (+ num_successes (:success-generation? val)) (+ num_generations (:num-generations val)) updated_list))
          (let [output   (gp/gp
                           (merge
                             {:instructions             instructions
                              :error-function           error-function
                              :error-function_2         nil
                              :training-data            (:train train-and-test-data)
                              :testing-data             (:test train-and-test-data)
                              :max-generations          500
                              :population-size          500
                              :max-initial-plushy-size  100
                              :solution-error-threshold 0.1
                              :step-limit               200
                              :parent-selection         :epsilon-lexicase
                              ;:parent-selection         :tournament
                              :tournament-size          5
                              :umad-rate                0.1
                              ;:variation                {:umad 0.5 :crossover 0.5}
                              :variation                {:umad 1.0}
                              ;:variation                {:umad-prob 0.05 :adjusted-plushy-mutation-prob 0.95}
                              ;:variation                {:umad-prob 0.30 :adjusted-plushy-mutation-prob 0.70 :crossover 0.0}
                              :elitism                  false
                              :isDefault                true}
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
            (recur (inc num_tries) (+ num_successes (:success-generation? val)) (+ num_generations (:num-generations val)) updated_list)))))))
