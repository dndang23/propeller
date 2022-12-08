(ns propeller.selection)

(defn tournament-selection
  "Selects an individual from the population using a tournament."
  [pop argmap]
  (let [tournament-size (:tournament-size argmap)
        tournament-set (take tournament-size (shuffle pop))]
    (apply min-key :total-error tournament-set)))

;(defn lexicase-selection
;  "Selects an individual from the population using lexicase selection."
;  [pop argmap]
;  (loop [survivors (map rand-nth (vals (group-by :case_total_error pop)))
;         cases (shuffle (range (count (:case_total_error (first pop)))))]
;    (if (or (empty? cases)
;            (empty? (rest survivors)))
;      (rand-nth survivors)
;      (let [min-err-for-case (apply min (map #(nth % (first cases))
;                                             (map :case_total_error survivors)))]
;        (recur (filter #(= (nth (:case_total_error %) (first cases)) min-err-for-case)
;                       survivors)
;               (rest cases))))))

(defn min_index
  [list]
  (loop [index 0 min_index 0]
    (if (= index (count list))
      (prn [min_index (nth list min_index)])
      (if (< (nth list index) (nth list min_index))
        (recur (inc index) index)
        (recur (inc index) min_index)))))

(defn lexicase-selection
  "Selects an individual from the population using lexicase selection."
  [pop argmap]
  (loop [survivors (map rand-nth (vals (group-by :errors pop)))
         cases (shuffle (range (count (:errors (first pop)))))]
    ;(println survivors)
    ;(println cases)
    ;(println (map #(count %) (map :errors survivors)))
    ;(println (min_index (map #(count %) (map :errors survivors))))
    ;(println (map #(nth % (first cases))
     ;             (map :errors survivors)))
    (if (or (empty? cases)
            (empty? (rest survivors)))
      (rand-nth survivors)
      (let [min-err-for-case (apply min (map #(nth % (first cases))
                                             (map :errors survivors)))]
        (recur (filter #(= (nth (:errors %) (first cases)) min-err-for-case)
                       survivors)
               (rest cases))))))

(defn epsilon-lexicase-selection
  [pop argmap]
  0)

(defn select-parent
  "Selects a parent from the population using the specified method."
  [pop argmap]
  (case (:parent-selection argmap)
    :tournament (tournament-selection pop argmap)
    :lexicase (lexicase-selection pop argmap)))
