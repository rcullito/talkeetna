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

(defn input-contains? [input delimiter]
  "search a given text string to see if it contains a delimiter"
  (re-find delimiter input))

(defn select-delimiter-from-input [input]
  "examine the input and determine the delimiter"
  (let [person-contains? (partial input-contains? input)]
    (cond
      ;; spaces will likely be present in pipe and comma delimited files
      ;; so check for those other types first
      (person-contains? comma-delimiter) comma-delimiter
      (person-contains? pipe-delimiter)  pipe-delimiter
      :else                              space-delimiter)))

(defn select-delimiter-from-filename [filename]
  "examine the file type and determine the delimeter"
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
