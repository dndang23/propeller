(ns propeller.problems.symbolic_regression_11
  (:require [propeller.genome :as genome]
            [propeller.push.interpreter :as interpreter]
            [propeller.push.state :as state]
            [propeller.tools.math :as math]
            [propeller.gp :as gp]
            #?(:cljs [cljs.reader :refer [read-string]])))

(defn- target-function
  "Target function: f(x) = (1 + x^2)^3 + 1"
  [x]
  (inc (* (inc (* x x)) (inc (* x x)) (inc (* x x)))))


(def train-and-test-data
  (let [train-inputs (range -1.5 1.5 0.1)
        test-inputs (range -1.75 1.75 0.05)]
    {:train (map (fn [x] {:input1 (vector x) :output1 (vector (target-function x))}) train-inputs)
     :test (map (fn [x] {:input1 (vector x) :output1 (vector (target-function x))}) test-inputs)}))

(def instructions
  (list :in1
        :float_add
        :float_subtract
        :float_mult
        :float_quot
        :float_dup
        0.0
        1.0))

(def kitchen_sink_instructions
  (list 0.0
        1.0
        :print_newline
        :integer_subtract
        :integer_inc
        :boolean_stack_depth
        :vector_integer_eq
        :boolean_pop
        :string_from_char
        :vector_string_shove
        :vector_float_yank_dup
        :exec_yank_dup
        :vector_integer_shove
        :integer_yank_dup
        :string_flush
        :boolean_swap
        :exec_shove
        :vector_boolean_yank
        :exec_y
        :boolean_yank
        :integer_eq
        :string_butlast
        :string_conj_char
        :vector_float_last
        :string_substr
        :integer_mult
        :in1
        :vector_string_dup_times
        :vector_integer_dup
        :boolean_or
        :boolean_empty
        :vector_string_print
        :vector_boolean_swap
        :char_dup_items
        :vector_float_pushall
        :char_is_whitespace
        :vector_string_replacefirst
        :string_first
        :vector_boolean_first
        :string_indexof_char
        :vector_float_replace
        :integer_from_string
        :char_from_integer
        :vector_integer_emptyvector
        :vector_string_eq
        :exec_dup_items
        :vector_float_butlast
        :boolean_dup_items
        :exec_empty
        :string_shove
        :vector_boolean_pushall
        :exec_rot
        :vector_string_concat
        :vector_float_indexof
        :vector_string_subvec
        :vector_integer_swap
        :char_pop
        :exec_dup
        :vector_integer_butlast
        :vector_float_rest
        :vector_string_flush
        :boolean_from_float
        :float_sin
        :boolean_flush
        :char_is_digit
        :float_lte
        :vector_integer_empty
        :code_print
        :vector_string_stack_depth
        :string_reverse
        :exec_k
        :vector_integer_yank
        :float_from_integer
        :char_rot
        :vector_integer_dup_times
        :char_print
        :vector_integer_stack_depth
        :vector_boolean_concat
        :boolean_xor
        :integer_gte
        :vector_float_shove
        :vector_integer_take
        :boolean_dup_times
        :string_replace_first
        :vector_integer_yank_dup
        :boolean_shove
        :float_lt
        :vector_string_dup
        :vector_string_occurrencesof
        :vector_integer_replace
        :vector_float_reverse
        :float_mod
        :vector_float_subvec
        :string_last
        :boolean_print
        :boolean_rot
        :vector_string_rest
        :integer_quot
        :vector_float_remove
        :integer_from_float
        :integer_lte
        :vector_integer_rot
        :integer_mod
        :string_concat
        :vector_string_butlast
        :vector_float_emptyvector
        :vector_string_yank_dup
        :integer_rot
        :float_yank_dup
        :vector_string_rot
        :vector_string_take
        :vector_float_dup_items
        :integer_add
        :vector_integer_occurrencesof
        :integer_shove
        :string_dup_times
        :char_swap
        :integer_max
        :vector_integer_flush
        :vector_integer_subvec
        :vector_boolean_indexof
        :vector_float_pop
        :char_dup_times
        :vector_string_remove
        :vector_integer_contains
        :code_append
        :vector_float_eq
        :vector_integer_conj
        :string_eq
        :integer_stack_depth
        :float_max
        :vector_boolean_set
        :vector_float_conj
        :float_dup_items
        :string_take
        :char_stack_depth
        :vector_integer_replacefirst
        :float_stack_depth
        :integer_dup_times
        :float_gt
        :boolean_dup
        :float_from_boolean
        :vector_float_replacefirst
        :vector_boolean_conj
        :exec_dup_times
        :vector_boolean_dup
        :vector_integer_indexof
        :vector_string_swap
        :exec_eq
        :string_empty_string
        :string_swap
        :integer_yank
        :exec_while
        :float_empty
        :vector_boolean_print
        :integer_min
        :exec_swap
        :vector_string_yank
        :string_stack_depth
        :string_replace_char
        :char_all_from_string
        :vector_integer_rest
        :vector_boolean_length
        :char_yank
        :vector_float_empty
        :string_pop
        :float_eq
        :integer_dup_items
        :vector_boolean_empty
        :vector_string_last
        :string_nth
        :vector_string_pop
        :vector_integer_nth
        :vector_integer_dup_items
        :exec_if
        :char_shove
        :vector_boolean_remove
        :vector_integer_remove
        :boolean_invert_first_then_and
        :string_print
        :integer_from_boolean
        :char_yank_dup
        :vector_string_first
        :boolean_from_integer
        :string_set_char
        :vector_integer_last
        :char_is_letter
        :vector_integer_concat
        :integer_print
        :boolean_eq
        :float_gte
        :string_occurencesof_char
        :string_replace_first_char
        :float_print
        :integer_flush
        :float_shove
        :string_replace
        :char_dup
        :float_pop
        :char_eq
        :vector_float_nth
        :vector_string_conj
        :integer_gt
        :vector_float_dup_times
        :float_subtract
        :vector_integer_length
        :vector_float_set
        :vector_string_indexof
        :vector_boolean_rest
        :vector_boolean_shove
        :float_min
        :boolean_not
        :float_mult
        :float_from_string
        :vector_boolean_dup_items
        :vector_integer_pop
        :vector_boolean_last
        :float_dec
        :vector_float_contains
        :string_empty
        :char_empty
        :exec_pop
        :vector_integer_set
        :vector_float_rot
        :string_yank_dup
        :string_remove_char
        :vector_string_replace
        :vector_float_first
        :char_flush
        :vector_float_occurrencesof
        :vector_string_emptyvector
        :float_add
        :exec_s
        :float_dup
        :vector_string_nth
        :vector_integer_reverse
        :vector_integer_print
        :char_from_float
        :integer_lt
        :vector_boolean_eq
        :vector_boolean_dup_times
        :string_contains_char
        :string_yank
        :vector_boolean_rot
        :float_swap
        :vector_string_pushall
        :vector_string_set
        :vector_boolean_flush
        :vector_boolean_stack_depth
        :vector_integer_pushall
        :vector_boolean_reverse
        :integer_swap
        :string_split
        :vector_boolean_contains
        :string_from_boolean
        :vector_float_dup
        :vector_boolean_replace
        :vector_string_dup_items
        :integer_dup
        :vector_boolean_nth
        :vector_string_length
        :string_rest
        :float_tan
        :string_rot
        :exec_yank
        :string_parse_to_chars
        :integer_pop
        :integer_empty
        :vector_float_flush
        :vector_float_yank
        :exec_print
        :float_dup_times
        :float_inc
        :vector_float_length
        :integer_dec
        :string_contains
        :vector_float_concat
        :vector_float_stack_depth
        :vector_integer_first
        :vector_float_print
        :float_rot
        :vector_string_contains
        :vector_boolean_occurrencesof
        :string_dup_items
        :vector_string_reverse
        :exec_stack_depth
        :float_flush
        :boolean_and
        :vector_boolean_butlast
        :string_length
        :float_cos
        :string_from_integer
        :exec_flush
        :vector_string_empty
        :exec_when
        :vector_float_swap
        :vector_boolean_pop
        :float_quot
        :vector_boolean_take
        :vector_float_take
        :boolean_invert_second_then_and
        :vector_boolean_subvec
        :float_yank
        :vector_boolean_emptyvector
        :vector_boolean_replacefirst
        :string_from_float
        :vector_boolean_yank_dup
        :string_dup
        :boolean_yank_dup))

(defn error-function
  "Finds the behaviors and errors of an individual. The error is the absolute
  deviation between the target output value and the program's selected behavior,
  or 1000000 if no behavior is produced. The behavior is here defined as the
  final top item on the FLOAT stack."
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
        (if (= type "probabilistic-05-95")
          (let [output   (gp/gp
                           (merge
                             {:instructions             kitchen_sink_instructions
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
                              :variation                {:umad-prob 0.05 :mutation-prob 0.95}
                              ;:variation                {:umad-prob 0.05 :perturbation-biased-mutation 0.95}
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
          (if (= type "probabilistic-20-80")
            (let [output   (gp/gp
                             (merge
                               {:instructions             kitchen_sink_instructions
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
                                :variation                {:umad-prob 0.20 :mutation-prob 0.80}
                                ;:variation                {:umad-prob 0.05 :perturbation-biased-mutation 0.95}
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
                               {:instructions             kitchen_sink_instructions
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
                                :variation                {:umad-prob 0.30 :mutation-prob 0.70}
                                ;:variation                {:umad-prob 0.05 :perturbation-biased-mutation 0.95}
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
              (recur (inc num_tries) (+ num_successes (:success-generation? val)) (+ num_generations (:num-generations val)) updated_list))))))))
