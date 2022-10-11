(ns propeller.problems.PSB2.gcd
  (:require [psb2.core :as psb2]
            [propeller.genome :as genome]
            [propeller.push.interpreter :as interpreter]
            [propeller.utils :as utils]
            [propeller.push.instructions :refer [get-stack-instructions]]
            [propeller.push.state :as state]
            [propeller.tools.math :as math]
            [propeller.gp :as gp]
            #?(:cljs [cljs.reader :refer [read-string]])))

; ===========  PROBLEM DESCRIPTION  ===============================
; GCD [GREATEST COMMON DIVISOR] from PSB2
; Given two integers, return the largest integer that divides each
; of the integers evenly
;
; Source: https://arxiv.org/pdf/2106.06086.pdf
; ==================================================================

(def train-and-test-data (psb2/fetch-examples "data" "gcd" 200 2000))

(defn random-int [] (- (rand-int 201) 100))

(defn map-vals-input
  "Returns all the input values of a map"
  [i]
  (vals (select-keys i [:input1 :input2])))

(defn map-vals-output
  "Returns the output values of a map"
  [i]
  (get i :output1))

(def instructions
  (utils/not-lazy
    (concat
      ;;; stack-specific instructions
      (get-stack-instructions #{:exec :integer :boolean :print})
      ;;; input instructions
      (list :in1 :in2)
      ;;; close
      (list 'close)
      ;;; ERCs (constants)
      (list random-int))))

(defn error-function
  [argmap data individual]
  (let [program (genome/plushy->push (:plushy individual) argmap)
        inputs (map (fn [i] (map-vals-input i)) data)
        correct-outputs (map (fn [i] (map-vals-output i)) data)
        outputs (map (fn [input]
                       (state/peek-stack
                         (interpreter/interpret-program
                           program
                           (assoc state/empty-state :input {:in1 (nth input 0)
                                                            :in2 (nth input 1)})
                           (:step-limit argmap))
                         :integer))
                     inputs)
        errors (map (fn [correct-output output]
                      (if (= output :no-stack-item)
                        1000000.0
                        (math/abs (- correct-output output))))
                    correct-outputs
                    outputs)]
    (assoc individual
      :behaviors outputs
      :errors errors
      :total-error #?(:clj  (apply +' errors)
                      :cljs (apply + errors)))))

(defn multiple-evaluation-function
  [argmap data individual]
  (loop [i 0 limit 5 behaviors_list [] error_list [] total_error_list []]
    (println "yo what is up dude")
    (if (= i limit)
      (assoc individual
        :behaviors behaviors_list
        :errors error_list
        :total-error total_error_list
        :average-error (float (/ #?(:clj  (apply +' total_error_list)
                                    :cljs (apply + total_error_list)) (count total_error_list))))
      (let [error_map (error-function argmap data error-function)
            behaviors (:behaviors error_map)
            errors (:errors error_map)
            total_error (:total-error total_error_list)]
        (recur (inc i) limit (conj behaviors_list behaviors) (conj error_list errors) (conj total_error_list total_error))))))

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
        (let [output (gp/gp
                       (merge
                         {:instructions            instructions
                          :error-function          multiple-evaluation-function
                          :training-data           (:train train-and-test-data)
                          :testing-data            (:test train-and-test-data)
                          :max-generations         300
                          :population-size         1000
                          :max-initial-plushy-size 250
                          :step-limit              2000
                          :parent-selection        :lexicase
                          :tournament-size         5
                          :umad-rate               0.1
                          :variation               {:umad-prob 0.05 :mutation-prob 0.95 :crossover 0.0}
                          :elitism                 false}
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
          (recur (inc num_tries) (+ num_successes (:success-generation? val)) (+ num_generations (:num-generations val)) updated_list)))))
  ;(#?(:clj shutdown-agents))
  )