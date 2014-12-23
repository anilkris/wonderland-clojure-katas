(ns doublets.solver-test
  (:require [clojure.test :refer :all]
            [doublets.solver :refer :all]
            [midje.sweet :refer :all]))

(deftest solver-test
  (testing "with word links found"
    (is (= ["head" "heal" "teal" "tell" "tall" "tail"]
           (doublets "head" "tail")))

    (is (= ["door" "boor" "book" "look" "lock"]
           (doublets "door" "lock")))

    (is (= ["bank" "bonk" "book" "look" "loon" "loan"]
           (doublets "bank" "loan")))

    (is (= ["wheat" "cheat" "cheap" "cheep" "creep" "creed" "breed" "bread"]
           (doublets "wheat" "bread"))))

  (testing "with no word links found"
    (is (= []
           (doublets "ye" "freezer")))))

(defn contains-l
  [c]
  (contains c :gaps-ok :in-any-order))

(fact "wordlist finds words of the same length as a given word, but that do not contain that word"
      (let [result (wordlist "loan")]
        result => (contains "book")
        result =not=> (contains "loan")))

(fact "zip is a helper function"
      (zip '(1 2 3) '(4 5 6)) => '((1 4) (2 5) (3 6)))

(tabular
 (fact "neighbors? finds words that are spelled similarly, with a difference of one letter"
       (neighbors? ?w1 ?w2) => ?bool)
 ?w1 ?w2 ?bool
 "boot" "boat" true
 "foo" "derp" false)

(fact "find-neighbors takes a word and a list and finds neighbors of the word in that list"
      (find-neighbors "teal" (wordlist "teal")) => (contains-l '("tell" "heal")))

(fact "rember removes a word completely from a sequence"
      (rember "derp" '("herp" "derp" "lerp" "derp")) => '("herp" "lerp"))

(def leaves-solution '(("door" "boor" "book" "look" "lock")))
(def solution-result (first leaves-solution))

(facts "about solutions"
       (fact "if the last item in the path is a neighbor of the solution, it will conj the goal onto the path"
             (solutions ["ye"] "me" '("the" "word" "list" "is" "irrelevant" "in" "this" "case"))
             =>
             '("ye" "me"))
       (fact "it does most of the dirty work of doublets, but look at how deep the solution is"
             (solutions ["door"] "lock" (wordlist "door")) =>
             '(((("door" "boor" "book" "look" "lock") (()))))))

(facts "about solution"
       (fact "it uses leaves"
             (leaves '(((("door" "boor" "book" "look" "lock") (())) ()))) => leaves-solution)
       (fact "it gives us the answer in the desired format"
             (solution '(((("door" "boor" "book" "look" "lock") (())) ())))
             => solution-result)
       (fact "it returns the best solution"
             (solution '(("door" "boor" "book" "look" "lock") ("door" "boor" "book" "look" "derp" "lock")))
             => solution-result))
