(ns alphabet-cipher.coder-test
  (:require [clojure.test :refer :all]
            [alphabet-cipher.coder :refer :all])
  (:use midje.sweet))

(facts "about rotate and the resulting chart"
       (fact "rotate works"
             (->> (rotate "abc") (take 1) first) => "bca")
       (tabular
        (fact "and we have a chart"
              (?fn chart) => ?result)
        ?fn ?result
        count 26
        first alphabet
        last "zabcdefghijklmnopqrstuvwxy"))

(facts "about lookup and numeric-lookup"
       (fact "numeric-lookup takes a numeric column and a row"
             (numeric-lookup 18 12) => "e")
       (fact "lookup works with letters"
             (lookup "s" "m") => "e"))

(facts "about encode"
       (fact "it can encode give a secret keyword"
             (encode "vigilance" "meetmeontuesdayeveningatseven") => "hmkbxebpxpmyllyrxiiqtoltfgzzv"
             (encode "scones" "meetmebythetree") => "egsgqwtahuiljgs"))

(facts "about decode"
       (fact "it can decode an encrypted message given a secret keyword"
             (decode "vigilance" "hmkbxebpxpmyllyrxiiqtoltfgzzv") => "meetmeontuesdayeveningatseven"
             (decode "scones" "egsgqwtahuiljgs") => "meetmebythetree"))
