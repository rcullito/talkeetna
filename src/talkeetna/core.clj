(ns gr.core
  (:require [clj-time.core :as t]
            [talkeetna.input :as i]
            [clj-time.format :as f]
            [clojure.pprint]))

(def custom-formatter (f/formatter "MM/dd/YYYY"))

(defn sort-gender-last-name [data]
  "sort by gender (females before males), last name ascending"
  (sort-by (juxt :gender :last-name) data))

(defn sort-dob [data]
  "sort by dob ascending"
  (sort-by #(f/parse custom-formatter (:dob %)) data))

(defn sort-last-name-desc [data]
  "sort by last name, descending"
  (reverse (sort-by :last-name data)))

(defn step-1 [filename]
  "putting together all of the above to satisfy requirements for step 1 of the exercise"
  ;; TODO maybe these first two lines should be handled by input.clj
  (let [delimiter       (i/select-delimiter filename)
        sortable-data   (i/parse-file filename delimiter)
        output-1        (sort-gender-last-name sortable-data)
        output-2        (sort-dob sortable-data)
        output-3        (sort-last-name-desc sortable-data)]
    {:output-1 output-1
     :output-2 output-2
     :output-3 output-3}))

(defn -main [filename]
  "entry point for cli tool"
  (-> filename
      step-1
      clojure.pprint/pprint))
