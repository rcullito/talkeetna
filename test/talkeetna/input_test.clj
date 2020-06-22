(ns talkeetna.input-test
  (:require [clojure.test :refer :all]
            [talkeetna.input :as i]))
;; TODO reuse these throughout the rest of the tests
(def piped-person "Gomez | Nicole | Female | Purple | 12/15/2000")
(def unpiped-person "Stein, David, Male, Blue, 02/08/1987") 

(deftest split-record-comma
  (testing "that splitting a record by a comma regex produces a vector of the expected length"
    (let [result (i/split-record i/comma-delimiter "Murphy, Colin, Male")]
      (is (true? (vector? result)))
      (is (= 3 (count result))))))


(deftest split-record-space
  (testing "that splitting a record by a space produces a vector of the expected length"
    (let [result (i/split-record i/space-delimiter "Murphy Colin Male")]
      (is (true? (vector? result)))
      (is (= 3 (count result))))))


(deftest split-record-pipe
  (testing "that splitting a record with a pipe produces a vector of the expected length"
    (let [result (i/split-record i/pipe-delimiter "Murphy | Colin | Male")]
      (is (true? (vector? result)))
      (is (= 3 (count result))))))


(deftest use-correct-delimeter
  (testing "that using different delimiters on the same record can produce vectors of different lengths")
  (let [record "Murphy | Colin | Male"
        pipe-result (i/split-record i/pipe-delimiter record)
        space-result (i/split-record i/space-delimiter record)]
    (is (> (count space-result) (count pipe-result)))))


(deftest trim-record-whitespace
  (testing "that trim-record fn effectively trims whitespace in a vector of strings")
  (let [whitespace        #"\s"
        whitespace-person ["Ericson  " " Berrit" " Female" "  Black" "04/14/1987 "]
        trimmed-person    (i/trim-record whitespace-person)]
    (is true? (every? nil? (map #(re-find whitespace %) trimmed-person)))))


(deftest build-map-from-vector
  (testing "that namevec->map returns a map"
    (let [person-attributes ["culliton" "rob" "male" "green" "09/18/1986"]]
      (is (true? (map? (i/namevec->map person-attributes)))))))

(deftest input-contains?-utility
  (testing "that this helper fn returns nil if the given delimiter is not in the input, and a result otherwise"
    (let [result-with-pipe    (i/input-contains? piped-person i/pipe-delimiter)
          result-without-pipe (i/input-contains? unpiped-person i/pipe-delimiter)]
      (is (some? result-with-pipe))
      (is (nil? result-without-pipe)))))

(deftest select-delimiter-input
  (testing "that the right delimiter is chosen for a given input"
    ))

(deftest select-delimiter-filename
  (testing "that the right delimiter is chosen for a given file extension"
    (is (= i/comma-delimiter (i/select-delimiter-from-filename "lions.csv")))
    (is (not= i/comma-delimiter (i/select-delimiter-from-filename "cats.ssv")))
    (is (= i/space-delimiter (i/select-delimiter-from-filename "cats.ssv")))
    (is (= i/pipe-delimiter (i/select-delimiter-from-filename "frogs.psv")))))


(deftest read-file-as-intended
  (testing "that we can successfully read from disk into a vector of strings"
    (let [list-of-people (i/file->vector-of-strings "people.csv")]
      (is true? (list? list-of-people))
      (is (= (count list-of-people) 5)))))
