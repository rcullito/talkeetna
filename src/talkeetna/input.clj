(ns talkeetna.input
  (:require [clojure.string :as s]))

(def space-delimiter  #" ")
(def comma-delimiter  #",")
(def pipe-delimiter  #"\|\s")

(defn split-record [delimiter record]
  "split the record into its individual components"
  (s/split record delimiter))

(defn trim-record [delimited-record]
  "trims whitespace from a list of strings"
  (map s/trim delimited-record))

(defn namevec->map [namevec]
  "build up an unlabelled vector of person attributes into a map with helpful keys"
  (apply sorted-map
         (interleave
          [:last-name :first-name :gender :color :dob]
          namevec)))

(defn select-delimiter [filename]
  "choose correct delimiter for file type"
  (let [file-ends? (partial s/ends-with? filename)]
    (cond
      (file-ends? "csv") comma-delimiter
      (file-ends? "psv") pipe-delimiter
      :else              space-delimiter)))

(defn file->vector-of-strings [filename]
  "reads a file from disk and returns a vector with each row as its own string"
  (-> filename
     slurp
     s/split-lines))
