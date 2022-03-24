(ns propeller.genome
  (:require [propeller.push.instructions :as instructions]
            [propeller.utils :as utils]))

(defn make-random-plushy2
  "Creates and returns a new plushy."
  [instructions max-initial-plushy-size]
  (let [plushy (repeatedly
                 (rand-int max-initial-plushy-size)
                 #(utils/random-instruction instructions))]
    (map #(conj [(rand)] %) plushy)))

(defn make-probability
  [plushy]
  [plushy (repeatedly (count plushy) rand)])

(defn make-random-plushy
  "Creates and returns a new plushy."
  [instructions max-initial-plushy-size]
  (repeatedly
    (rand-int max-initial-plushy-size)
    #(utils/random-instruction instructions)))

(make-probability (make-random-plushy [1 2 3] 10))

(defn plushy->push
  "Returns the Push program expressed by the given plushy representation."
  ([plushy] (plushy->push (make-probability plushy) {}))
  ([[plushy prob] argmap]
   (let [plushy (if (:diploid argmap) (map first (partition 2 plushy)) plushy)
         opener? #(and (vector? %) (= (first %) 'open))]    ;; [open <n>] marks opens
     (loop [push ()                                         ;; iteratively build the Push program from the plushy
            plushy (mapcat #(let [n (get instructions/opens %)]
                              (if (and n
                                       (> n 0))
                                [% ['open n]]
                                [%]))
                           plushy)
            prob_list prob]
       (if (empty? plushy)                                  ;; maybe we're done?
         (if (some opener? push)                            ;; done with plushy, but unclosed open
           (recur push '(close) '(close))                            ;; recur with one more close
           push)                                            ;; otherwise, really done, return push
         (let [i (first plushy)
               p (first prob_list)]
           (if (< (rand) p)
             (if (= i 'close)
               (if (some opener? push)                      ;; process a close when there's an open
                 (recur (let [post-open (reverse (take-while (comp not opener?)
                                                             (reverse push)))
                              open-index (- (count push) (count post-open) 1)
                              num-open (second (nth push open-index))
                              pre-open (take open-index push)]
                          (if (= 1 num-open)
                            (concat pre-open [post-open])
                            (concat pre-open [post-open ['open (dec num-open)]])))
                        (rest plushy)
                        (rest prob_list))
                 (recur push (rest plushy) (rest prob_list))))               ;; unmatched close, ignore
             (recur (concat push [i]) (rest plushy) (rest prob_list))))))))) ;; anything else

(plushy->push (make-random-plushy [1 2 3 "integer_add" "exec_if"] 10))