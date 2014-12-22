(ns doublets.solver
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]))

(defn dictionary
  [file]
  (-> file
      io/resource
      slurp
      read-string
      set
      seq))

(def words (dictionary "words.edn"))

(defn rember
  "Removes an atom from a sequence of atoms."
  [a sat]
  (remove (partial = a) sat))

(defn wordlist
  "Given a word, finds words in the word list of the same length (but are not that word)."
  [word]
  (let [c (count word)
        f #(= (count %) c)]
    (->> words
         (filter f)
         (rember word))))

(defn zip
  [& things]
  (->> things
    (apply interleave)
    (partition (count things))))

(defn neighbors?
  "Two words are neighbors if they are spelled the same except for one letter."
  [word1 word2]
  (->> (zip word1 word2)
       (map (partial apply not=))
       (filter true?)
       count
       (= 1)))

(defn find-neighbors
  [word wordlist]
  (filter (partial neighbors? word) wordlist))

(defn solutions
  [path goal wordlist]
  (lazy-seq
   (when-let [w (seq wordlist)] ;; if there are no words left, return nil
     (let [l (last path)]
       (if (neighbors? l goal)
         (conj path goal)
         (when-let [candidates (find-neighbors l w)] ;; if there are no candidates left, return nil
           (map #(solutions (conj path %) goal (rember % w)) candidates)))))))

(defn leaves
  "Given a tree of solutions, return the solutions as a list."
  [input]
  (let [tree (->> input (tree-seq (partial some sequential?) seq) rest)
        ;; unlike flatten, we want lists, not atoms
        ;; but not empty lists, or lists that contain lists
        ;; we make those checks into one predicate so we only have to do one remove pass
        has-sequences? (partial some sequential?)
        solution-predicate (fn [x] (or (empty? x) (has-sequences? x)))]
    (remove solution-predicate tree)))

(defn solution
  "Given a list of solutions, find the shortest solution."
  [i]
  (->> (leaves i) (sort-by <) first))

(defn doublets [word1 word2]
  (let [c (count word1)
        c2 (count word2)]
    (if (= c c2)
      (->> (wordlist word1) (solutions [word1] word2) solution)
      [])))
