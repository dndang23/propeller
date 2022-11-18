(ns propeller.gp
  (:require [clojure.string]
            [clojure.pprint]
            [propeller.genome :as genome]
            [propeller.simplification :as simplification]
            [propeller.variation :as variation]
            [propeller.push.instructions.bool]
            [propeller.push.instructions.character]
            [propeller.push.instructions.code]
            [propeller.push.instructions.input-output]
            [propeller.push.instructions.numeric]
            [propeller.push.instructions.polymorphic]
            [propeller.push.instructions.string]
            [propeller.push.instructions.vector]))

(defn report
  "Reports information each generation."
  [pop generation argmap]
  (let [best (first pop)]
    (clojure.pprint/pprint {:generation            generation
                            :best-plushy           (:plushy best)
                            :best-program          (genome/plushy->push (:plushy best) argmap)
                            :best-total-error      (:total-error best)
                            :best-errors           (:errors best)
                            :best-behaviors        (:behaviors best)
                            :genotypic-diversity   (float (/ (count (distinct (map :plushy pop))) (count pop)))
                            :behavioral-diversity  (float (/ (count (distinct (map :behaviors pop))) (count pop)))
                            :average-genome-length (float (/ (reduce + (map count (map :plushy pop))) (count pop)))
                            :average-total-error   (float (/ (reduce + (map :total-error pop)) (count pop)))})
    (println)))

;(defn that_plushy
;  [list]
;  (loop [index 0]
;    (if (= index (count list))
;      nil
;      (let [plushy (nth list index)]
;        (if (= (:total-error plushy) 99999)
;          plushy
;          (recur (inc index)))))))

; solution threshold = 0.1 (for now)
(defn gp
  "Main GP loop."
  [{:keys [population-size max-generations error-function instructions
           max-initial-plushy-size solution-error-threshold mapper]
    :or   {solution-error-threshold 0.0
           ;; The `mapper` will perform a `map`-like operation to apply a function to every individual
           ;; in the population. The default is `map` but other options include `mapv`, or `pmap`.
           mapper #?(:clj map :cljs map)}
    :as   argmap}]
  ;;
  ;(prn {:starting-args (update (update argmap :error-function str) :instructions str)})
  (println "Executing gp")
  (println)

  (println "Arguments used in genetic program:")
  (prn {:starting-args (update (update argmap :error-function str) :instructions str)})
  (println)

  ;;
  (loop [generation 0
         population (mapper
                      (fn [_] {:plushy (genome/make-random-plushy instructions max-initial-plushy-size)})
                      (range population-size))]
    (let [evaluated-pop (sort-by :total-error
                                 (mapper
                                   (partial error-function argmap (:training-data argmap))
                                   population))
          best-individual (first evaluated-pop)]
      (if (:custom-report argmap)
        ((:custom-report argmap) evaluated-pop generation argmap))
      ;(report evaluated-pop generation argmap))
      (println)
      (println "the best individual for this run is:")
      (prn best-individual)
      (println)
      ;(if (some? (that_plushy evaluated-pop))
      ;  (do
      ;    (prn (that_plushy evaluated-pop))))
      (cond
        ;; Success on training cases is verified on testing cases
        (<= (:total-error best-individual) solution-error-threshold)
        (do (println "Completed run")
            (println)
            (println "The best individual according to the genetic program is:")
            (println best-individual)
            (println)
            {:success-generation generation})
        ;(do (prn {:success-generation generation})
        ;(prn {:total-test-error
        ;      (:total-error (error-function argmap (:testing-data argmap) best-individual))})
        ;(when (:simplification? argmap)
        ;  (let [simplified-plushy (simplification/auto-simplify-plushy (:plushy best-individual) error-function argmap)]
        ;    (prn {:total-test-error-simplified (:total-error (error-function argmap (:testing-data argmap) (hash-map :plushy simplified-plushy)))}))))
        ;;
        (>= generation max-generations)
        nil
        ;;
        :else (recur (inc generation)
                     (if (:elitism argmap)
                       (conj (repeatedly (dec population-size)
                                         ; need to redo implementation
                                         #(variation/new-individual evaluated-pop argmap))
                             (first evaluated-pop))
                       (repeatedly population-size
                                   ; need to redo implementation
                                   #(variation/new-individual evaluated-pop argmap))))))))


; best program: (in_1 in_3)

; probabilistic plushy ([in_1 p_1] [in_2 p_2] [in_3 p_3])
; boolean plushy ([in_1 true] [in_2 false] [in_3 true])