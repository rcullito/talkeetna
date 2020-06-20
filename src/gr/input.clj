(def space-delimiter  #" ")
(def comma-delimiter  #",")
(def pipe-delimiter  #"\|\s")


(defn select-delimiter [filename]
  "choose correct delimiter for file type"
  (let [file-extension (last (s/split filename #"\."))
        file-ends?     (partial s/ends-with? filename)]
    (cond
      (file-ends? "csv") comma-delimiter
      (file-ends? "psv") pipe-delimiter
      (file-ends? "ssv") space-delimiter)))

(defn split-person [delimiter person]
  "split the person string into its individual components"
  (s/split person delimiter))

(defn trim-person [delimited-person]
  "trims whitespace from the previously delimited record"
  (map s/trim delimited-person))

;; this function name is counterintuitive
(defn namevec->map [namevec]
  "build a vector of person attributes into a map"
  (apply sorted-map
         (interleave
          [:last-name :first-name :gender :color :dob]
          namevec)))

(defn parse-file [filename delimiter]
  "parse the file into a vector of strings for each person"
  (-> filename
    slurp
    s/split-lines
    (->> (map (partial split-person delimiter))
         (map trim-person)
         (map namevec->map))))

