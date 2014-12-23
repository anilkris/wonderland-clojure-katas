(ns wonderland-number.finder)

(def wonderland-range (range 100000 1000000))

(defn digit-set
  [n] (-> n str set))

(defn hasAllTheSameDigits? [n1 n2]
  (let [s1 (digit-set n1)
        s2 (digit-set n2)]
    (= s1 s2)))

(defn samedigits
  [multiplicand number]
  (hasAllTheSameDigits? (* number multiplicand) number))

(defn wonder-predicate
  [n]
  (let [r (range 2 7)
        preds (map #(samedigits % n) r)]
    (= (distinct preds) '(true))))

(defn wonderland-number []
  (->> wonderland-range
       (filter wonder-predicate)
       first))
