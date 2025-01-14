(ns propeller.problems.PSB2.fuel-cost
  (:require [psb2.core :as psb2]
            [propeller.genome :as genome]
            [propeller.push.interpreter :as interpreter]
            [propeller.utils :as utils]
            [propeller.push.instructions :refer [get-stack-instructions]]
            [propeller.push.state :as state]
            [propeller.tools.math :as math]
            [propeller.gp :as gp]
            #?(:cljs [cljs.reader :refer [read-string]])))

; ===========  PROBLEM DESCRIPTION  =========================
; FUEL COST from PSB2
; Given a vector of positive integers, divide
; each by 3, round the result down to the nearest integer, and
; subtract 2. Return the sum of all of the new integers in the
; vector
;
; Source: https://arxiv.org/pdf/2106.06086.pdf
; ============================================================

(def train-and-test-data (psb2/fetch-examples "data" "fuel-cost" 200 2000))

; Random integer between -100 and 100 (from smallest)
(defn random-int [] (- (rand-int 201) 100))

(def instructions
  (utils/not-lazy
    (concat
      ;;; stack-specific instructions
      (get-stack-instructions #{:exec :integer :boolean :vector_integer :print})
      ;;; input instructions
      (list :in1)
      ;;; close
      (list 'close)
      ;;; ERCs (constants)
      (list random-int 0 1 2 3))))

(defn error-function
  [argmap data individual]
  (let [program (genome/plushy->push (:plushy individual) argmap)
        inputs (map (fn [i] (get i :input1)) data)
        correct-outputs (map (fn [i] (get i :output1)) data)
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
                      :cljs (apply + errors)))))

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
                           {:instructions            instructions
                            :error-function          error-function
                            :training-data           (:train train-and-test-data)
                            :testing-data            (:test train-and-test-data)
                            :max-generations         300
                            :population-size         1000
                            :max-initial-plushy-size 250
                            :step-limit              2000
                            :parent-selection        :lexicase
                            :tournament-size         5
                            :umad-rate               0.1
                            :variation               {:umad 1.0 :crossover 0.0}
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
          (recur (inc num_tries) (+ num_successes (:success-generation? val)) (+ num_generations (:num-generations val)) updated_list))))))