(ns talkeetna.core
  (:require [clj-time.core :as t]
            [clojure.pprint]
            [talkeetna.sort :as so]
            [talkeetna.input :as i]))

;; code in this namespace is generally used for running Step 1:
;; the command line app

;; we basically do two things:
;; 1. parse data
;; 2. return according to desired sort order

(defn parse-file [filename]
  "combine several helper fns from input.clj such that 
   we take a file and return a sequence of correctly keyed maps"
  (let [delimiter (i/select-delimiter-from-filename filename)]
    (->> (i/file->vector-of-strings filename)
         (map (partial i/split-record delimiter))
         (map i/trim-record)
         (map i/namevec->map))))

(defn assemble-sorts [keyed-data]
  "takes already assembled seq of maps and returns 3 different sort orders"
  {:gender-last-name     (so/sort-gender-last-name keyed-data)
   :birth-date-ascending (so/sort-dob keyed-data)
   :last-name-descending (so/sort-last-name-desc keyed-data)})

(defn -main [filename]
  "entry point for cli tool"
  (let [all-sort-orders (-> filename
                          parse-file
                          assemble-sorts)]
    ;; when run from the cli, we want to print
    ;; but also returning a result is useful for testing
    (do (clojure.pprint/pprint all-sort-orders)
        all-sort-orders)))
