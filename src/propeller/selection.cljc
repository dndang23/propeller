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

(defn math-abs
  [n]
  (if (< n 0)
    (* -1 n)
    n))

(defn mean [coll]
  (let [sum (apply + coll)
        count (count coll)]
    (if (pos? count)
      (/ sum (float count))
      0)))

(defn median [coll]
  (let [sorted (sort coll)
        cnt (count sorted)
        halfway (quot cnt 2.0)]
    (if (odd? cnt)
      (nth sorted halfway) ; (1)
      (let [bottom (dec halfway)
            bottom-val (nth sorted bottom)
            top-val (nth sorted halfway)]
        (mean [bottom-val top-val]))))) ; (2)

(defn median-absolute-deviation
  [coll]
  (let [median-val (median coll)]
    ;(print (map #(math-abs (- % median-val)) coll))
    (median (map #(math-abs (- % median-val)) coll))))

(defn epsilon-list
  [pop]
  (let [error-list (map :errors pop)
        length (count (:errors (first pop)))]
    ;(println error-list)
    ;(println length)
    ;(println (map #(nth % 0) error-list))
    (loop [epsilons [] i 0]
     ;(println (median-absolute-deviation (map #(nth % 0) error-list)))
     ;(println epsilons)
      (if (= i length)
        epsilons
        (recur (conj epsilons (median-absolute-deviation (map #(nth % i) error-list))) (inc i))))))

;(epsilon-list [{:errors '(1 2 3)} {:errors '(0 0 0)} {:errors '(1 1 1)}])

(defn epsilon-lexicase-selection
  "Selects an individual from the population using lexicase selection."
  [pop argmap]
  (let [epsilons (epsilon-list pop)]
    (loop [survivors (map rand-nth (vals (group-by :errors pop)))
           cases (shuffle (range (count (:errors (first pop)))))]
      (if (or (empty? cases)
              (empty? (rest survivors)))
        (rand-nth survivors)
        (let [min-err-for-case (apply min (map #(nth % (first cases))
                                               (map :errors survivors)))
              epsilon (nth epsilons (first cases))]
          (recur (filter #(<= (math-abs (- (nth (:errors %) (first cases)) min-err-for-case)) epsilon)
                         survivors)
                 (rest cases)))))))

(defn select-parent
  "Selects a parent from the population using the specified method."
  [pop argmap]
  (case (:parent-selection argmap)
    :tournament (tournament-selection pop argmap)
    :lexicase (lexicase-selection pop argmap)
    :epsilon-lexicase (epsilon-lexicase-selection pop argmap)))
