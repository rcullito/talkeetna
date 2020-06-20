(ns talkeetna.core
  (:require [clj-time.core :as t]
            [clojure.pprint]
            [talkeetna.input :as i]))

;; code in this namespace is generally used for running Step 1:
;; the command line app

(defn parse-file [filename delimiter]
  "combine several helper fns from input.clj such that 
   we take a file and return a sequence of correctly keyed maps"
  (->> (i/file->vector-of-strings filename)
       (map (partial i/split-record delimiter))
       (map i/trim-record)
       (map i/namevec->map)))


;; TODO this could be clearer
;; we basically do two things:
;; 1. parse data
;; 2. return sorted according to variations
(defn step-1 [filename]
  "putting together all of the above to satisfy requirements for step 1 of the exercise"
  (let [delimiter            (i/select-delimiter filename)
        unsorted-data        (parse-file filename delimiter)
        gender-last-name     (sort-gender-last-name unsorted-data)
        birth-date-ascending (sort-dob unsorted-data)
        last-name-descending  (sort-last-name-desc unsorted-data)]
    {:gender-last-name     gender-last-name
     :birth-date-ascending birth-date-ascending
     :last-name-descending last-name-descending}))

(defn -main [filename]
  "entry point for cli tool"
  (-> filename
      step-1
      clojure.pprint/pprint))
