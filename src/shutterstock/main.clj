(ns shutterstock.main
  (:gen-class)
  (:require [hickory.core :as hc]
            [hickory.select :as hs]
            [clojure.spec.alpha :as spec]
            [clojure.string :refer [lower-case]]))


(defn sample-html
  "Given a query, slurp shutterstock for the first assoicated image.  The :query is a string
  whose words are concatinated with a '+'.  For example \"girls+horses\""
  [{:keys [query people ethnicity
           gender age]}]
  (let [people? (if people (str "&mreleased=" people) "")
        age? (if age (str "&age=" age) "")
        ethnicity? (if ethnicity (str "&ethnicity=" ethnicity) "")
        gender? (if gender (str "&gender=" gender "&mreleased=true") "")
        image-type "photo"
        orientation "horizontal"]
    (slurp (str "https://www.shutterstock.com/search/"
                query "?image_type=" image-type "&orientation=" orientation
                people? ethnicity? gender? age?
                ))))

;; alpha-numeric characters only, exclude spaces.
(def query-regex #"^[a-zA-Z0-9+]*$")
(spec/def ::query-type (spec/and string? #(re-matches query-regex %)))
(spec/def ::query ::query-type)

(spec/def ::people #(or (nil? %)
                        (= % "true")
                        (= % "false")))

(spec/def ::ethnicity #(or (nil? %)
                           (= % "japanese")
                           (= % "caucasian")
                           (= % "black")
                           (= % "southeast-asian")
                           (= % "hispanic")
                           (= % "chinese")))

(spec/def ::gender #(or (nil? %)
                        (= % "male")
                        (= % "female")
                        (= % "both")))

(spec/def ::age #(or (nil? %)
                     (= % "infants") (= % "children")
                     (= % "teenagers") (= % "20s")
                     (= % "30s") (= % "40s")
                     (= % "50s") (= % "60s") (= % "older")
                     ))

(spec/def ::cell #(or (nil? %)
                       (and (integer? %)
                            (>= % 0))))

(defn valid-search-terms?
  "Ensure that the query is a valid string"
  [{:keys [query people ethnicity
           gender age cell]}]
  (and (spec/valid? ::query query)
       (spec/valid? ::ethnicity ethnicity)
       (spec/valid? ::gender gender)
       (spec/valid? ::age age)
       (spec/valid? ::people people)
       (spec/valid? ::cell cell)))


(defn format-args
  "Ensure args from user args are lower cased. Convert cell to integer"
  [{:keys [query ethnicity people gender age cell]}]
  (let [parse-int #(Integer/parseInt (re-find #"\A-?\d+" %))]
    (merge {} {:query (lower-case query)}
         (when ethnicity {:ethnicity (lower-case ethnicity)})
         (when people {:people (lower-case people)}) 
         (when gender {:gender (lower-case gender)})
         (when age {:age (lower-case age)})
         (when cell {:cell (parse-int cell)}))))


(defn -main
  "Parse the Shutter Stock Page to retrieve the first image url that matches the
  search input criteria, and copy it to the clipboard. Queries are hashmaps:
  $ clj -X:run :query horses+girls age: teenagers"
  [{:as args}]
  (let [error-message "Usage: :query <string> :age <string> :gender <string> :people <boolean> :ethnicity <string>d"
        bad-query "No image matching query was found"
        result? #(if (nil? %) bad-query %)
        args'  (format-args args)
        rand-img #(nth % (rand-int 10))
        cell (get args' :cell)
        get-img #(if cell (nth % cell) (rand-img %))
        ]
    (if (valid-search-terms? args')
      (->> (sample-html args')
          (hc/parse)
          (hc/as-hickory)
          (hs/select (hs/class "z_g_d65b1"))
          first
          (hs/select (hs/tag :img))
          get-img
          :attrs :src
          result?
          println)
      (println error-message))))
