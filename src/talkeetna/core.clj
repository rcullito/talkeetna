(ns talkeetna.core
  (:require [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.pprint]
            [talkeetna.input :as i]))

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

;; TODO this is really about combining input
;; so it feels like it belongs here rather than with the purer
;; fns of input.clj
(defn parse-file [filename delimiter]
  "parse the file into a seq of maps containing individual fields"
  (->> (i/file->vector-of-strings filename)
       (map (partial i/split-record delimiter))
       (map i/trim-record)
       (map i/namevec->map)))

(defn step-1 [filename]
  "putting together all of the above to satisfy requirements for step 1 of the exercise"
  ;; TODO maybe these first two lines should be handled by input.clj
  (let [delimiter            (i/select-delimiter filename)
        sortable-data        (parse-file filename delimiter)
        gender-last-name     (sort-gender-last-name sortable-data)
        birth-date-ascending (sort-dob sortable-data)
        last-name-descending  (sort-last-name-desc sortable-data)]
    {:gender-last-name     gender-last-name
     :birth-date-ascending birth-date-ascending
     :last-name-descending last-name-descending}))

(defn -main [filename]
  "entry point for cli tool"
  (-> filename
      step-1
      clojure.pprint/pprint))
