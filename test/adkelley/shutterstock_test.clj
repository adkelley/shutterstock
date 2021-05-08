(ns adkelley.shutterstock-test
  (:require [clojure.test :refer [deftest is testing]]
            [adkelley.shutterstock :refer [valid-search-terms? vals-lowercase]]))

(deftest valid-query-test
  (testing "valid-search-terms? ::query"
    (is (= (valid-search-terms? (vals-lowercase {:query "Girls"})) true))
    (is (= (valid-search-terms? (vals-lowercase {:query "horses+Girls"})) true))
    (is (= (valid-search-terms? (vals-lowercase {:query "Horses gIrls"})) false))
    (is (= (valid-search-terms? {:query 1}) false))
    (is (= (valid-search-terms? {:query nil}) false))
    (is (= (valid-search-terms? {:wrong-key "girls"}) false))
    ))

(deftest valid-people-test
  (testing "valid-search-terms? ::people"
    (is (= (valid-search-terms? (vals-lowercase {:query "Girls"})) true))
    (is (= (valid-search-terms? (vals-lowercase {:query "Girls" :people true})) true))
    (is (= (valid-search-terms? {:query "girls" :people false}) true))
    (is (= (valid-search-terms? {:query "girls" :people "maybe"}) false))
    (is (= (valid-search-terms? {:query "girls" :people ""}) false))
    ))

(deftest valid-age-test
  (testing "valid-search-terms ::age"
    (is (= (valid-search-terms? (vals-lowercase {:query "girls" :age "inFants"})) true))
    (is (= (valid-search-terms? {:query "girls" :age "children"}) true))
    (is (= (valid-search-terms? {:query "girls" :age "teenagers"}) true))
    (is (= (valid-search-terms? (vals-lowercase {:query "girls" :age "20s"})) true))
    (is (= (valid-search-terms? {:query "girls" :age "30s"}) true))
    (is (= (valid-search-terms? {:query "girls" :age "40s"}) true))
    (is (= (valid-search-terms? {:query "girls" :age "50s"}) true))
    (is (= (valid-search-terms? {:query "girls" :age "60s"}) true))
    (is (= (valid-search-terms? {:query "girls" :age "older"}) true))
    ))

(deftest valid-gender-test
  (testing "valid-search-terms ::gender"
    (is (= (valid-search-terms? {:query "girls"}) true))
    (is (= (valid-search-terms? {:query "girls" :gender "male"}) true))
    (is (= (valid-search-terms? {:query "girls" :gender "female"}) true))
    (is (= (valid-search-terms? {:query "girls" :gender "both"}) true))
    (is (= (valid-search-terms? {:query "girls" :gender ""}) false))
    ))

(deftest valid-ethnicity-test
  (testing "valid-search-terms ::ethnicity")
    (is (= (valid-search-terms? {:query "girls" :ethnicity "black"}) true))
    (is (= (valid-search-terms? (vals-lowercase {:query "girls" :ethnicity "Japanese"})) true))
    (is (= (valid-search-terms? (vals-lowercase {:query "girls" :ethnicity "caUcasian"})) true))
    (is (= (valid-search-terms? {:query "girls" :ethnicity "southeast-asian"}) true))
    (is (= (valid-search-terms? {:query "girls" :ethnicity "hispanic"}) true))
    (is (= (valid-search-terms? {:query "girls" :ethnicity "chinese"}) true))
    (is (= (valid-search-terms? {:query "girls" :ethnicity ""}) false))
    (is (= (valid-search-terms? {:query "girls"}) true)))
