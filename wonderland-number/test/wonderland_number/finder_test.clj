(ns wonderland-number.finder-test
  (:require [clojure.test :refer :all]
            [midje.sweet :refer :all]
            [wonderland-number.finder :refer :all]))

(tabular
 (fact "wonderland-range is a range that contains only 6 digit numbers"
       (-> wonderland-range ?fn str count) => 6)
 ?fn
 first
 last)

(tabular
 (fact "hasAllTheSameDigits? works, thanks Gigasquid!"
       (hasAllTheSameDigits? ?n 1))
 ?n 1 2 0 36 42)

(fact "about samedigits"
      (-> #(samedigits % 0)
          (map '(1 2 3 4 5 6))
          distinct) => '(true))

(fact "about wonder-predicate"
      (wonder-predicate 0) => true)

(facts "about wonderland-number"
       (let [wondernum (wonderland-number)]
         (fact "it must have 6 digits"
               (-> wondernum str count) => 6)                
         (tabular 
          (fact "if you multiply it by 2, 3, 4, 5, or 6, the result should have the same digits (but they may bein a different order)"
                (hasAllTheSameDigits? wondernum (* ?multiplicand wondernum)) => true)
          ?multiplicand 2 3 4 5 6)

         (let [n (-> (/ 1 7) double str)
               rrest (comp rest rest)
               rn (rrest n)
               part (-> (partition 6 rn) distinct first)
               mathnum (read-string (apply str part))]
           
           (fact "it is found by dividing 1/7"
                 (= mathnum wondernum) => true)
           (fact "it has some other interesting properties"
                 (let [digits (map (comp read-string str) part)
                       digitsum (apply + digits)]
                   (/ mathnum digitsum) => integer?)))))


