(ns propeller.variation
    (:require [propeller.selection :as selection]
              [propeller.utils :as utils]))

;new mutation operator
;probabilistic plushy
;mutation operator tweaking the probabilities

(defn crossover
  "Crosses over two individuals using uniform crossover. Pads shorter one."
  [plushy-a plushy-b]
  (let [shorter (min-key count plushy-a plushy-b)
        longer (if (= shorter plushy-a)
                 plushy-b
                 plushy-a)
        length-diff (- (count longer) (count shorter))
        shorter-padded (concat shorter (repeat length-diff :crossover-padding))]
    (remove #(= % :crossover-padding)
            (map #(if (< (rand) 0.5) %1 %2)
                 shorter-padded
                 longer))))

(defn tail-aligned-crossover
  "Crosses over two individuals using uniform crossover. Pads shorter one on the left."
  [plushy-a plushy-b]
  (let [shorter (min-key count plushy-a plushy-b)
        longer (if (= shorter plushy-a)
                 plushy-b
                 plushy-a)
        length-diff (- (count longer) (count shorter))
        shorter-padded (concat (repeat length-diff :crossover-padding) shorter)]
    (remove #(= % :crossover-padding)
            (map #(if (< (rand) 0.5) %1 %2)
                 shorter-padded
                 longer))))

(defn diploid-crossover
  "Crosses over two individuals using uniform crossover. Pads shorter one."
  [plushy-a plushy-b]
  (let [plushy-a (partition 2 plushy-a)
        plushy-b (partition 2 plushy-b)
        shorter (min-key count plushy-a plushy-b)
        longer (if (= shorter plushy-a)
                 plushy-b
                 plushy-a)
        length-diff (- (count longer) (count shorter))
        shorter-padded (concat shorter (repeat length-diff :crossover-padding))]
    (flatten (remove #(= % :crossover-padding)
                     (map #(if (< (rand) 0.5) %1 %2)
                          shorter-padded
                          longer)))))

(defn tail-aligned-diploid-crossover
  "Crosses over two individuals using uniform crossover. Pads shorter one on the left."
  [plushy-a plushy-b]
  (let [plushy-a (partition 2 plushy-a)
        plushy-b (partition 2 plushy-b)
        shorter (min-key count plushy-a plushy-b)
        longer (if (= shorter plushy-a)
                 plushy-b
                 plushy-a)
        length-diff (- (count longer) (count shorter))
        shorter-padded (concat (repeat length-diff :crossover-padding) shorter)]
    (flatten (remove #(= % :crossover-padding)
                     (map #(if (< (rand) 0.5) %1 %2)
                          shorter-padded
                          longer)))))
;original uniform addition
(defn uniform-addition
  "Returns plushy with new instructions possibly added before or after each
  existing instruction."
  [plushy instructions umad-rate]
  (apply concat
         (map #(if (< (rand) umad-rate)
                 (shuffle [% (utils/random-instruction instructions)])
                 [%])
              plushy)))

;new uniform addition
(defn prob-uniform-addition
  "Returns plushy with new instructions possibly added before or after each
  existing instruction."
  [plushy instructions umad-rate]
  (apply concat
         (map #(if (< (rand) umad-rate)
                 (shuffle [%
                           [(utils/random-instruction instructions) (rand)]])
                 [%])
              plushy)))

;(prob-uniform-addition '([:integer_add 0.9504416885390561] [true 0.41428932725239154] [1 0.5576250442969661] ["exec_if" 0.7222062772248353])
;                       ["hi" "hello" "sad"]
;                       0.5)

;original uniform replacement
(defn uniform-replacement
  "Returns plushy with new instructions possibly replacing existing
   instructions."
  [plushy instructions replacement-rate]
  (map #(if (< (rand) replacement-rate)
          (utils/random-instruction instructions)
          %)
       plushy))

;new uniform replacement
;(defn prob-uniform-replacement
;  "Returns plushy with new instructions possibly replacing existing
;   instructions."
;  [plushy instructions replacement-rate]
;  (map #(if (< (rand) replacement-rate)
;          [(utils/random-instruction instructions) (rand)]
;          %)
;       plushy))

(defn gaussian-noise-factor
  "Returns gaussian noise of mean 0, std dev 1."
  []
  (*' (Math/sqrt (*' -2.0 (Math/log (rand))))
      (Math/cos (*' 2.0 Math/PI (rand)))))

(defn perturb-with-gaussian-noise
  "Returns n perturbed with std dev sd."
  [sd n]
  (+' n (*' sd (gaussian-noise-factor))))

;(defn getPosVal
;  []
;  (loop [temp (gaussian-noise-factor)]
;    (if (> temp 0)
;      temp
;      (recur (gaussian-noise-factor)))))

;(defn getNegVal
;  []
;  (loop [temp (gaussian-noise-factor)]
;    (if (< temp 0)
;      temp
;      (recur (gaussian-noise-factor)))))

(defn perturb-with-positive-gaussian-noise
  "Returns n perturbed with std dev sd."
  [sd n]
  (let [val (+ n (* sd (gaussian-noise-factor)) sd)]
    (if (> val 1)
      1
      (if (< val 0)
        0
        val))))

(defn perturb-with-negative-gaussian-noise
  "Returns n perturbed with std dev sd."
  [sd n]
  (let [val (+ n (* sd (gaussian-noise-factor)))
        val_2 (- val sd)]
    (if (< val_2 0)
      0
      (if (> val_2 1)
        1
        val_2))))

;mutates the probabilities of plushy
(defn prob-mutation
  [plushy]
  (map #(if (< (rand) 1)
          [(first %) (perturb-with-gaussian-noise 0.01 (last %))]
          %)
       plushy))

;(defn checker-instr-in-plushy
;  [instr best-plushy i]
;  (let [boolean-plushy (last best-plushy)
;        length (count boolean-plushy)]
;    (if (> i (- length 1))
;      false
;      (let [best-tuple (nth boolean-plushy i)
;            best-instr (first best-tuple)
;            best-bool (last best-tuple)]
;        (if (and (= instr best-instr) (= best-bool true))
;          true
;          false)))))

(defn adjusted-plushy-prob-mutation
  [plushy-hash-table]
  (let [plushy (:plushy plushy-hash-table)
        min-program-boolean-plushy (:min-program-boolean-plushy plushy-hash-table)]
    (loop [i (- (count plushy) 1) new-plushy '()]
      (if (= i -1)
        new-plushy
        (let [cur-plushy (nth plushy i)
              cur-min-program-boolean-plushy (nth min-program-boolean-plushy i)]
          (if (= (last cur-min-program-boolean-plushy) true)
            (recur (dec i) (conj new-plushy [(first cur-plushy) (perturb-with-positive-gaussian-noise 0.3 (last cur-plushy))]))
            (recur (dec i) (conj new-plushy [(first cur-plushy) (perturb-with-negative-gaussian-noise 0.3 (last cur-plushy))]))))))))

;(defn best-plushy-prob-mutation
;  [plushy]
;  (loop [i (- (count plushy) 1)
;         new_plushy '()]
;    (if (= i -1)
;      new_plushy
;      (let [cur-plushy (nth plushy i)
;            plushy-instruction (first cur-plushy)
;            is-in-best-plushy (checker-instr-in-plushy plushy-instruction best-plushy i)]
;        (if (= is-in-best-plushy true)
;          (recur (dec i) (conj new_plushy [(first cur-plushy) (perturb-with-positive-gaussian-noise 0.01 (last cur-plushy))]))
;          (recur (dec i) (conj new_plushy [(first cur-plushy) (perturb-with-negative-gaussian-noise 0.01 (last cur-plushy))])))))))

;(= :in1 :in1)
;(best-plushy-prob-mutation '([:in1 0.7192694405822808] [0.08692005434008143 0.803755695111884] [:float_mult 0.6934938590786862] [:float_sin 0.8479153181578405]) ['(:in1 :float_cos :float_add 0.527870165162838) '([:in1 true] [:float_cos true] [:float_add false] [0.527870165162838 true])])

(defn diploid-uniform-silent-replacement
  "Returns plushy with new instructions possibly replacing existing
   instructions, but only among the silent member of each pair."
  [plushy instructions replacement-rate]
  (interleave (map first (partition 2 plushy))
              (map #(if (< (rand) replacement-rate)
                      (utils/random-instruction instructions)
                      %)
                   (map second (partition 2 plushy)))))

(defn diploid-uniform-addition
  "Returns plushy with new instructions possibly added before or after each
  existing instruction."
  [plushy instructions umad-rate]
  (flatten
    (map (fn [pair]
           (if (< (rand) umad-rate)
             (shuffle [pair (repeatedly 2 #(utils/random-instruction instructions))])
             [pair]))
         (partition 2 plushy))))

(defn uniform-deletion
  "Randomly deletes instructions from plushy at some rate."
  [plushy umad-rate]
  (if (zero? umad-rate)
    plushy
    (remove (fn [_] (< (rand)
                       (/ 1 (+ 1 (/ 1 umad-rate)))))
            plushy)))

(defn diploid-uniform-deletion
  "Randomly deletes instructions from plushy at some rate."
  [plushy umad-rate]
  (flatten (remove (fn [_] (< (rand)
                              (/ 1 (+ 1 (/ 1 umad-rate)))))
                   (partition 2 plushy))))

(defn diploid-flip
  "Randomly flips pairs in a diploid plushy at some rate."
  [plushy flip-rate]
  (flatten (map #(if (< (rand) flip-rate)
                   (reverse %)
                   %)
                (partition 2 plushy))))

(defn new-individual
  "Returns a new individual produced by selection and variation of
  individuals in the population."
  [pop argmap]
  {:plushy
   (let [r (rand)
         op (loop [accum 0.0
                   ops-probs (vec (:variation argmap))]
              (if (empty? ops-probs)
                :reproduction
                (let [[op1 prob1] (first ops-probs)]
                  (if (>= (+ accum prob1) r)
                    op1
                    (recur (+ accum prob1)
                           (rest ops-probs))))))]
     (case op
       :crossover
       (crossover
         (:plushy (selection/select-parent pop argmap))
         (:plushy (selection/select-parent pop argmap)))
       ;
       :tail-aligned-crossover
       (tail-aligned-crossover
         (:plushy (selection/select-parent pop argmap))
         (:plushy (selection/select-parent pop argmap)))
       ;
       :umad
       (-> (:plushy (selection/select-parent pop argmap))
           (uniform-addition (:instructions argmap) (:umad-rate argmap))
           (uniform-deletion (:umad-rate argmap)))
       ;
       :umad-prob
       (-> (:plushy (selection/select-parent pop argmap))
           (prob-uniform-addition (:instructions argmap) (:umad-rate argmap))
           (uniform-deletion (:umad-rate argmap)))
       ;
       :mutation-prob
       (-> (:plushy (selection/select-parent pop argmap))
           (prob-mutation))
       ;
       :adjusted-plushy-mutation-prob
       (-> (selection/select-parent pop argmap)
           (adjusted-plushy-prob-mutation))
       ;
       :rumad
       (let [parent-genome (:plushy (selection/select-parent pop argmap))
             after-addition (uniform-addition parent-genome
                                              (:instructions argmap)
                                              (:umad-rate argmap))
             effective-addition-rate (/ (- (count after-addition)
                                           (count parent-genome))
                                        (count parent-genome))]
         (uniform-deletion after-addition effective-addition-rate))
       ;
       :uniform-addition
       (-> (:plushy (selection/select-parent pop argmap))
           (uniform-addition (:instructions argmap) (:umad-rate argmap)))
       ;
       :uniform-replacement
       (-> (:plushy (selection/select-parent pop argmap))
           (uniform-replacement (:instructions argmap) (:replacement-rate argmap)))
       ;
       :diploid-uniform-silent-replacement
       (-> (:plushy (selection/select-parent pop argmap))
           (diploid-uniform-silent-replacement (:instructions argmap) (:replacement-rate argmap)))
       ;
       :uniform-deletion
       (-> (:plushy (selection/select-parent pop argmap))
           (uniform-deletion (:umad-rate argmap)))
       ;
       :diploid-crossover
       (diploid-crossover
         (:plushy (selection/select-parent pop argmap))
         (:plushy (selection/select-parent pop argmap)))
       ;
       :tail-aligned-diploid-crossover
       (tail-aligned-diploid-crossover
         (:plushy (selection/select-parent pop argmap))
         (:plushy (selection/select-parent pop argmap)))
       ;
       :diploid-umad
       (-> (:plushy (selection/select-parent pop argmap))
           (diploid-uniform-addition (:instructions argmap) (:umad-rate argmap))
           (diploid-uniform-deletion (:umad-rate argmap)))
       ;
       :diploid-uniform-addition
       (-> (:plushy (selection/select-parent pop argmap))
           (diploid-uniform-addition (:instructions argmap) (:umad-rate argmap)))
       ;
       :diploid-uniform-deletion
       (-> (:plushy (selection/select-parent pop argmap))
           (diploid-uniform-deletion (:umad-rate argmap)))
       ;
       :diploid-flip
       (-> (:plushy (selection/select-parent pop argmap))
           (diploid-flip (:diploid-flip-rate argmap)))
       ;
       :reproduction
       (:plushy (selection/select-parent pop argmap))
       ;
       :else
       (throw #?(:clj  (Exception. (str "No match in new-individual for " op))
                 :cljs (js/Error
                         (str "No match in new-individual for " op))))))})
