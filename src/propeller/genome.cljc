(ns propeller.genome
  (:require [propeller.push.instructions :as instructions]
            [propeller.utils :as utils]))

;Original function to create plushy
;(defn make-random-plushy
;  "Creates and returns a new plushy."
;  [instructions max-initial-plushy-size]
;  (repeatedly
;    (rand-int max-initial-plushy-size)
;    #(utils/random-instruction instructions)))

; adds probability to plushy
(defn add-probability-to-plushy
  [plushy]
  (map #(conj [%] (rand)) plushy))

; creates a random plushy and then adds probability to each instruction
(defn make-random-plushy
  "Creates and returns a new plushy."
  [isDefault instructions max-initial-plushy-size]
  (let [plushy (repeatedly
                 (rand-int max-initial-plushy-size)
                 #(utils/random-instruction instructions))]
        ;prob-plushy (add-probability-to-plushy plushy)]
        (if (= isDefault true)
          plushy
          (add-probability-to-plushy plushy))))
;    plushy))

;(make-random-plushy (list :in1
;                          :integer_add
;                          :integer_subtract
;                          :integer_mult
;                          :integer_quot
;                          :integer_eq
;                          :exec_dup
;                          :exec_if
;                          'close
;                          0
;                          1)
 ;                   20)

; translates plushy with probability to regular plushy
; used to create a subset of plushy with probability
;(defn plushy-with-prob->plushy
;  [plushy-with-prob]
;  ;(println plushy-with-prob)
;  (filter identity (map (fn [[thing prob]]
;                          (if (< (rand) prob)
;                            thing
;                            nil))
;                        plushy-with-prob)))

;(plushy-with-prob->plushy (make-random-plushy [1 2 "integer_add" "exec_if" true false 4] 5))

; translates plushy into a push program
(defn plushy->push
  "Returns the Push program expressed by the given plushy representation."
  ([plushy] (plushy->push plushy {}))
  ([plushy argmap]
   (let [;prob-plushy (plushy-with-prob->plushy plushy)
         ;plushy (if (:diploid argmap) (map first (partition 2 prob-plushy)) prob-plushy)
         plushy (if (:diploid argmap) (map first (partition 2 plushy)) plushy)
         opener? #(and (vector? %) (= (first %) 'open))]    ;; [open <n>] marks opens
     (loop [push ()                                         ;; iteratively build the Push program from the plushy
            plushy (mapcat #(let [n (get instructions/opens %)]
                              (if (and n
                                       (> n 0))
                                [% ['open n]]
                                [%]))
                           plushy)]
       (if (empty? plushy)                                  ;; maybe we're done?
         (if (some opener? push)                            ;; done with plushy, but unclosed open
           (recur push '(close))                            ;; recur with one more close
           push)                                            ;; otherwise, really done, return push
         (let [i (first plushy)]
           (if (= i 'close)
             (if (some opener? push)                        ;; process a close when there's an open
               (recur (let [post-open (reverse (take-while (comp not opener?)
                                                           (reverse push)))
                            open-index (- (count push) (count post-open) 1)
                            num-open (second (nth push open-index))
                            pre-open (take open-index push)]
                        (if (= 1 num-open)
                          (concat pre-open [post-open])
                          (concat pre-open [post-open ['open (dec num-open)]])))
                      (rest plushy))
               (recur push (rest plushy)))                  ;; unmatched close, ignore
             (recur (concat push [i]) (rest plushy))))))))) ;; anything else