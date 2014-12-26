(ns alphabet-cipher.coder)

;; Utils

(defn bimap
  [a b]
  (into (zipmap a b)
        (zipmap b a)))

(defn rotate
  [string]
  (let [f (str (first string))
        r (clojure.string/join (rest string))
        result (str r f)]
    (lazy-seq
     (cons
      result
      (rotate result)))))

;; Variables

(def alphabet "abcdefghijklmnopqrstuvwxyz")

(def chart (cons alphabet (take 25 (rotate alphabet))))

(def lookup-map
  (bimap (map str alphabet) (iterate inc 0)))

;; Functions

(defn encoding
  [a b]
  (let [row (lookup-map a)
        column (lookup-map b)]
    (-> chart
        (nth row)
        (nth column)
        str)))

(defn decoding
  [row letter]
  (let [num (lookup-map row)]
    (-> chart
        (nth num)
        (.indexOf letter)
        lookup-map
        str)))

(defn coding
  [f]
  (fn [keyword input]
    (->> input
         (interleave (cycle keyword))
         (map str)
         (partition 2)
         (mapcat #(apply f %))
         (apply str))))

(def encode (coding encoding))

(def decode (coding decoding))
