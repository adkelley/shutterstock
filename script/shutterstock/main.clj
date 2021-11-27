(ns shutterstock.main
  (:require [babashka.pods :as pods]
            [hickory.select :as hs]
            [clojure.walk :refer [keywordize-keys]]
            [clojure.string :refer [lower-case]]))

(pods/load-pod 'retrogradeorbit/bootleg "0.1.9")

(require '[pod.retrogradeorbit.bootleg.utils :as utils])

(defn sample-html
  "Given a query, slurp shutterstock for the first associated image.  The :query is a string
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
  search input criteria, and copy it to the clipboard Queries are NOT hash maps:
  $ bb -m shutterstock.main query horses+girls"
  [& args]
  (let [bad-query "No image matching query was found"
        args' (format-args (keywordize-keys (apply hash-map args)))
        result? #(if (nil? %) bad-query %)
        rand-img #(nth % (rand-int 10))
        cell (get args' :cell)
        get-img #(if cell (nth % cell) (rand-img %))
        ]
      (->> (utils/convert-to (sample-html args') :hickory)
           (hs/select (hs/class "z_g_d65b1"))
          first
          (hs/select (hs/tag :img))
          get-img
          :attrs :src
          result?
          print)))
