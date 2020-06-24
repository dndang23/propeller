(ns propeller.push.instructions.string
  (:require [propeller.push.instructions :refer [def-instruction]]
            [propeller.push.utils :refer [make-instruction]]))

(def-instruction
  :string_=
  (fn [state]
    (make-instruction state = [:string :string] :boolean)))

(def-instruction
  :string_concat
  (fn [state]
    (make-instruction state #(apply str (concat %1 %2)) [:string :string] :string)))

(def-instruction
  :string_drop
  (fn [state]
    (make-instruction state #(apply str (drop %1 %2)) [:integer :string] :string)))

(def-instruction
  :string_includes?
  (fn [state]
    (make-instruction state clojure.string/includes? [:string :string] :boolean)))

(def-instruction
  :string_length
  (fn [state]
    (make-instruction state count [:string] :integer)))

(def-instruction
  :string_reverse
  (fn [state]
    (make-instruction state #(apply str (reverse %)) [:string] :string)))

(def-instruction
  :string_take
  (fn [state]
    (make-instruction state #(apply str (take %1 %2)) [:integer :string] :string)))
